package com.mironov.coursework.ui.chat

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.mironov.coursework.R
import com.mironov.coursework.databinding.ChangeMessageDialogBinding
import com.mironov.coursework.databinding.DialogMessageActionBinding
import com.mironov.coursework.databinding.FragmentChatBinding
import com.mironov.coursework.domain.entity.Message
import com.mironov.coursework.presentation.chat.ChatCommand
import com.mironov.coursework.presentation.chat.ChatEffect
import com.mironov.coursework.presentation.chat.ChatEvent
import com.mironov.coursework.presentation.chat.ChatInfo
import com.mironov.coursework.presentation.chat.ChatState
import com.mironov.coursework.ui.adapter.DelegateItem
import com.mironov.coursework.ui.adapter.MainAdapter
import com.mironov.coursework.ui.chat.date.DateDelegate
import com.mironov.coursework.ui.chat.received.ReceivedDelegate
import com.mironov.coursework.ui.chat.sent.SentDelegate
import com.mironov.coursework.ui.chat.topic.MessageTopicDelegate
import com.mironov.coursework.ui.main.ElmBaseFragment
import com.mironov.coursework.ui.reaction.ChooseReactionDialogFragment
import com.mironov.coursework.ui.utils.appComponent
import com.mironov.coursework.ui.utils.hide
import com.mironov.coursework.ui.utils.show
import com.mironov.coursework.ui.utils.showDialog
import com.mironov.coursework.ui.utils.showErrorSnackBar
import vivid.money.elmslie.android.renderer.elmStoreWithRenderer
import vivid.money.elmslie.core.store.ElmStore
import vivid.money.elmslie.core.store.Store
import javax.inject.Inject

class ChatFragment : ElmBaseFragment<ChatEffect, ChatState, ChatEvent>() {

    companion object {
        fun newInstance(chatInfo: ChatInfo) = ChatFragment().apply {
            arguments = Bundle().apply {
                putParcelable(CHAT_INFO_KEY, chatInfo)
            }
        }

        private const val EMPTY_STRING = ""
        private const val CHAT_INFO_KEY = "chat info"
        private const val TARGET_POSITION = 5
    }

    private val component by lazy {
        requireContext().appComponent().getChatComponentFactory().create()
    }

    private var channelName = EMPTY_STRING
    private var topicName = EMPTY_STRING
    private var channelId = ChatInfo.UNDEFINED_ID
    private var firstTopic = EMPTY_STRING

    private var canLoadNextPage = false
    private var isNeedLoadNextPage = false
    private var isNeedLoadPrevPage = false

    private var _binding: FragmentChatBinding? = null
    private val binding: FragmentChatBinding
        get() = requireNotNull(_binding)

    @Inject
    lateinit var chatStore: ElmStore<ChatEvent, ChatState, ChatEffect, ChatCommand>

    override val store: Store<ChatEvent, ChatEffect, ChatState> by elmStoreWithRenderer(
        elmRenderer = this
    ) {
        chatStore
    }

    val adapter by lazy {
        MainAdapter().apply {
            addDelegate(DateDelegate())
            addDelegate(
                ReceivedDelegate(
                    ::chooseReaction,
                    ::changeReaction,
                    ::onMessageLongClickListener
                )
            )
            addDelegate(
                SentDelegate(
                    ::chooseReaction,
                    ::changeReaction,
                    ::onMessageLongClickListener
                )
            )
            addDelegate(MessageTopicDelegate(::onTopicClickListener))
        }
    }

    override fun onAttach(context: Context) {
        component.inject(this)
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseArguments()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        store.accept(ChatEvent.Ui.Load(channelName, topicName, channelId))
        initChatParams()
        setupRecyclerView()
        addClickListeners()
        addTextWatcher()
    }

    override fun render(state: ChatState) {
        if (state.isLoading)
            applyLoadingState()

        canLoadNextPage = !state.isNextPageLoading
        isNeedLoadNextPage = state.isNeedLoadNextPage
        isNeedLoadPrevPage = state.isNeedLoadPrevPage

        state.topicNameList?.let(::initChooseTopicAdapter)
        state.content?.let(::applyContentState)
    }

    override fun handleEffect(effect: ChatEffect): Unit = when (effect) {
        ChatEffect.ErrorLoadingMessages -> applyErrorLoadingState()

        is ChatEffect.ErrorSendingMessage -> applySendingError()

        is ChatEffect.ErrorChangeReaction -> applyChangeReactionError()

        is ChatEffect.ShowMessageActionDialog -> showMessageActionDialog(effect)

        is ChatEffect.ShowEditTopicDialog -> showEditTopicDialog(effect)

        is ChatEffect.ShowEditMessageDialog -> showEditMessageDialog(effect)

        ChatEffect.ErrorChangeMessage -> showErrorSnackBar(getString(R.string.error_change_message))

        ChatEffect.ErrorChangeTopic -> showErrorSnackBar(getString(R.string.error_change_topic))

        ChatEffect.ErrorDeleteMessage -> showErrorSnackBar(getString(R.string.error_delete_message))

        ChatEffect.ErrorLoadingNewPage -> showErrorSnackBar(getString(R.string.error_loading_messages))
    }

    private fun initChatParams() {
        requireActivity().window.statusBarColor = requireContext().getColor(R.color.primary_color)

        with(binding) {
            toolbar.title = "#$channelName"
            if (topicName.isNotEmpty()) {
                currentTopic.isVisible = true
                chooseTopic.isVisible = false
                currentTopic.text = String.format(getString(R.string.topic_with_name), topicName)
            } else {
                chooseTopic.isVisible = true
                currentTopic.isVisible = false
            }
        }
    }

    @Suppress("DEPRECATION")
    private fun parseArguments() {
        val args = requireArguments()
        val chatInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            args.getParcelable(CHAT_INFO_KEY, ChatInfo::class.java)
        else
            args.getParcelable(CHAT_INFO_KEY)

        chatInfo?.let {
            topicName = it.topicName
            channelName = it.channelName
            channelId = it.channelId
        }
    }

    private fun addTextWatcher() {
        binding.messageInput.doOnTextChanged { text, _, _, _ ->
            changeButtonIcon(text?.trim().isNullOrEmpty())
        }
    }

    private fun setupRecyclerView() {
        binding.messages.adapter = adapter
        binding.messages.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                if (dy > 0) {
                    val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()
                    val totalItemCount = layoutManager.itemCount
                    if (totalItemCount - lastVisibleItemPosition <= TARGET_POSITION
                        && canLoadNextPage
                        && isNeedLoadNextPage
                    ) {
                        store.accept(ChatEvent.Ui.ScrollToBottom(channelName, topicName))
                    }
                }

                if (dy < 0) {
                    val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
                    if (firstVisibleItemPosition <= TARGET_POSITION
                        && canLoadNextPage
                        && isNeedLoadPrevPage
                    ) {
                        store.accept(ChatEvent.Ui.ScrollToTop(channelName, topicName))
                    }
                }
            }
        })
    }

    private fun addClickListeners() = with(binding) {
        sendMessage.setOnClickListener {
            sendMessage(binding.messageInput.text?.trim().toString())
        }

        toolbar.setNavigationOnClickListener {
            store.accept(ChatEvent.Ui.OnBackClicked)
        }

        binding.tryAgain.setOnClickListener {
            store.accept(ChatEvent.Ui.Load(channelName, topicName, channelId))
        }
    }

    fun changeReaction(messageId: Long, emojiName: String, isSelected: Boolean) {
        store.accept(ChatEvent.Ui.ChangeReaction(messageId, emojiName, isSelected))
    }

    private fun applyContentState(delegateList: List<DelegateItem>) {
        with(binding) {
            shimmer.hide()
            chatErrorMessage.isVisible = false
            tryAgain.isVisible = false
            messages.isVisible = true
            messages.adapter = adapter
        }
        adapter.submitList(delegateList)
    }

    private fun applyLoadingState() {
        with(binding) {
            shimmer.show()
            chatErrorMessage.isVisible = false
            tryAgain.isVisible = false
            messages.isVisible = false
        }
    }

    private fun applyErrorLoadingState() {
        with(binding) {
            shimmer.hide()
            chatErrorMessage.isVisible = true
            tryAgain.isVisible = true
            messages.isVisible = false
            chatErrorMessage.text = getString(R.string.error_loading_messages)
        }
    }

    private fun applySendingError() {
        with(binding) {
            shimmer.hide()
            chatErrorMessage.isVisible = false
            tryAgain.isVisible = false
            messages.isVisible = true
        }
        showErrorSnackBar(getString(R.string.error_sending_message))
    }

    private fun applyChangeReactionError() {
        with(binding) {
            shimmer.hide()
            chatErrorMessage.isVisible = false
            tryAgain.isVisible = false
            messages.isVisible = true
        }
        showErrorSnackBar(getString(R.string.error_change_reaction))
    }

    private fun sendMessage(text: String) {
        if (text.isNotEmpty()) {
            if (topicName == ChatInfo.EMPTY_STRING)
                sendMessageToChosenTopic(text)
            else
                sendMessageToCurrentTopic(text)
        }
    }

    private fun sendMessageToCurrentTopic(text: String) {
        store.accept(ChatEvent.Ui.SendMessage(channelName, topicName, text))
        binding.messageInput.text?.clear()
    }

    private fun sendMessageToChosenTopic(text: String) {
        val topic = if (binding.chooseTopic.text.isNullOrEmpty())
            firstTopic
        else
            binding.chooseTopic.text.toString()

        store.accept(ChatEvent.Ui.SendMessage(channelName, topic, text))
        binding.messageInput.text?.clear()
    }

    private fun changeButtonIcon(inputIsEmpty: Boolean) {
        val iconId = if (inputIsEmpty) R.drawable.ic_upload else R.drawable.ic_send_message

        binding.sendMessage.setImageResource(iconId)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun onTopicClickListener(topic: String) {
        store.accept(
            ChatEvent.Ui.OnTopicClicked(
                chatInfo = ChatInfo(
                    topicName = topic,
                    channelName = channelName
                )
            )
        )
    }

    private fun chooseReaction(messageId: Long) {
        val dialog = ChooseReactionDialogFragment.newInstance(messageId)
        dialog.show(requireActivity().supportFragmentManager, ChooseReactionDialogFragment.TAG)
        dialog.onEmojiClickedCallback = ::acceptChooseReaction
    }

    private fun onMessageLongClickListener(message: Message) {
        store.accept(ChatEvent.Ui.OnMessageLongClicked(message))
    }

    private fun acceptChooseReaction(messageId: Long, emojiName: String) {
        store.accept(ChatEvent.Ui.ChooseReaction(messageId, emojiName))
    }

    private fun initChooseTopicAdapter(topics: List<String>) {
        firstTopic = topics.first()
        val adapter = ArrayAdapter(requireContext(), R.layout.chat_topic_item, topics)
        binding.chooseTopic.setAdapter(adapter)
        binding.chooseTopic.hint = topics.first()
    }

    private fun showMessageActionDialog(effect: ChatEffect.ShowMessageActionDialog) {
        val dialog = BottomSheetDialog(requireContext())
        val dialogBinding = DialogMessageActionBinding.inflate(layoutInflater)
        dialogBinding.initViews(
            effect = effect,
            onDismiss = {
                dialog.dismiss()
            }
        )

        dialog.apply {
            setContentView(dialogBinding.root)
            show()
        }
    }

    private fun showEditTopicDialog(effect: ChatEffect.ShowEditTopicDialog) {
        val dialogLayout = ChangeMessageDialogBinding.inflate(layoutInflater)
        dialogLayout.changeInput.setText(effect.oldTopic)

        showDialog(
            view = dialogLayout.root,
            positiveButtonTextId = R.string.save,
            positiveButtonClickListener = {
                saveChangeTopic(
                    messageId = effect.messageId,
                    topic = dialogLayout.changeInput.text?.trim().toString(),
                )
            }
        )
    }

    private fun showEditMessageDialog(effect: ChatEffect.ShowEditMessageDialog) {
        val dialogLayout = ChangeMessageDialogBinding.inflate(layoutInflater)
        dialogLayout.changeInput.setText(effect.oldMessage)

        showDialog(
            view = dialogLayout.root,
            positiveButtonTextId = R.string.save,
            positiveButtonClickListener = {
                saveChangeMessage(
                    messageId = effect.messageId,
                    message = dialogLayout.changeInput.text?.trim().toString(),
                )
            }
        )
    }

    private fun saveChangeTopic(messageId: Long, topic: String) {
        if (topic.isNotEmpty())
            store.accept(ChatEvent.Ui.SaveNewTopic(messageId, topic))
    }

    private fun saveChangeMessage(messageId: Long, message: String) {
        store.accept(ChatEvent.Ui.SaveNewMessage(messageId, message))
    }

    private fun DialogMessageActionBinding.initViews(
        effect: ChatEffect.ShowMessageActionDialog,
        onDismiss: () -> Unit
    ) {
        setVisibility(isVisible = effect.isContentEditable, icEditMessage, editMessage)
        setVisibility(isVisible = effect.canDelete, icDelete, delete)
        setVisibility(isVisible = effect.isTopicEditable, icEditTopic, editTopic)

        addClickListener(
            onClick = {
                store.accept(
                    ChatEvent.Ui.OnEditMessageTopicClicked(
                        messageId = effect.message.id,
                        oldTopic = effect.message.topicName
                    )
                )
                onDismiss()
            },
            icEditTopic, editTopic
        )

        addClickListener(
            onClick = {
                store.accept(
                    ChatEvent.Ui.OnEditMessageContentClicked(
                        messageId = effect.message.id,
                        oldMessage = effect.message.content
                    )
                )
                onDismiss()
            },
            icEditMessage, editMessage
        )

        addClickListener(
            onClick = {
                store.accept(
                    ChatEvent.Ui.OnDeleteTopicClicked(effect.message.id)
                )
                onDismiss()
            },
            icDelete, delete
        )

        addClickListener(
            onClick = {
                chooseReaction(effect.message.id)
                onDismiss()
            },
            icEmoji, addReaction
        )

        addClickListener(
            onClick = {
                store.accept(
                    ChatEvent.Ui.OnCopyMessageTextClicked(
                        context = requireContext(),
                        text = effect.message.content
                    )
                )
                onDismiss()
            },
            icCopy, copy
        )
    }

    private fun setVisibility(isVisible: Boolean, vararg views: View) {
        views.forEach {
            it.isVisible = isVisible
        }
    }

    private fun addClickListener(
        onClick: () -> Unit,
        vararg views: View
    ) {
        views.forEach {
            it.setOnClickListener {
                onClick()
            }
        }
    }
}