package com.mironov.coursework.ui.chat

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import com.mironov.coursework.ui.main.ElmBaseFragment
import com.mironov.coursework.R
import com.mironov.coursework.databinding.FragmentChatBinding
import com.mironov.coursework.presentation.chat.ChatEffect
import com.mironov.coursework.presentation.chat.ChatEvent
import com.mironov.coursework.presentation.chat.ChatState
import com.mironov.coursework.presentation.chat.ChatStoreFactory
import com.mironov.coursework.ui.adapter.DelegateItem
import com.mironov.coursework.ui.adapter.MainAdapter
import com.mironov.coursework.ui.chat.date.DateDelegate
import com.mironov.coursework.ui.chat.received.ReceivedDelegate
import com.mironov.coursework.ui.chat.sent.SentDelegate
import com.mironov.coursework.ui.main.MainActivity
import com.mironov.coursework.ui.reaction.ChooseReactionDialogFragment
import com.mironov.coursework.ui.utils.hide
import com.mironov.coursework.ui.utils.show
import com.mironov.coursework.ui.utils.showErrorSnackBar
import vivid.money.elmslie.android.renderer.elmStoreWithRenderer
import vivid.money.elmslie.core.store.Store
import javax.inject.Inject

class ChatFragment : ElmBaseFragment<ChatEffect, ChatState, ChatEvent>() {

    companion object {
        fun newInstance(channelName: String, topicName: String) = ChatFragment().apply {
            arguments = Bundle().apply {
                putString(CHANNEL_NAME_KEY, channelName)
                putString(TOPIC_NAME_KEY, topicName)
            }
        }

        private const val CHANNEL_NAME_KEY = "channel name"
        private const val TOPIC_NAME_KEY = "topic name"
    }

    private val component by lazy {
        (requireActivity() as MainActivity).component
    }

    private var channelName = ""
    private var topicName = ""

    private var _binding: FragmentChatBinding? = null
    private val binding: FragmentChatBinding
        get() = _binding!!

    @Inject
    lateinit var chatStoreFactory: ChatStoreFactory

    override val store: Store<ChatEvent, ChatEffect, ChatState> by elmStoreWithRenderer(
        elmRenderer = this
    ) {
        chatStoreFactory.create()
    }

    val adapter by lazy {
        MainAdapter().apply {
            addDelegate(DateDelegate())
            addDelegate(ReceivedDelegate(::chooseReaction, ::changeReaction))
            addDelegate(SentDelegate(::chooseReaction, ::changeReaction))
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
        store.accept(ChatEvent.Ui.Load(channelName, topicName))
        initStatusBar()
        addClickListeners()
        addTextWatcher()
    }

    override fun render(state: ChatState) {
        if (state.isLoading) {
            applyLoadingState()
        }

        state.content?.let {
            applyContentState(it)
        }
    }

    override fun handleEffect(effect: ChatEffect): Unit = when (effect) {
        ChatEffect.ErrorLoadingMessages -> {
            applyErrorLoadingState()
        }

        is ChatEffect.ErrorSendingMessage -> {
            applySendingError(effect.messages)
        }

        is ChatEffect.ErrorChangeReaction -> {
            applyChangeReactionError(effect.messages)
        }
    }

    private fun initStatusBar() {
        requireActivity().window.statusBarColor = requireContext()
            .getColor(R.color.primary_color)
        binding.toolbar.title = "#$channelName"
    }

    private fun parseArguments() {
        val args = requireArguments()
        topicName = args.getString(TOPIC_NAME_KEY, "")
        channelName = args.getString(CHANNEL_NAME_KEY, "")
    }

    private fun addTextWatcher() {
        binding.messageInput.doOnTextChanged { text, _, _, _ ->
            changeButtonIcon(text?.trim().isNullOrEmpty())
        }
    }

    private fun addClickListeners() = with(binding) {
        sendMessage.setOnClickListener {
            sendMessage()
        }
        toolbar.setNavigationOnClickListener {
            store.accept(ChatEvent.Ui.OnBackClicked)
        }

        binding.tryAgain.setOnClickListener {
            store.accept(ChatEvent.Ui.Load(channelName, topicName))
        }
    }

    fun changeReaction(messageId: Long, emojiName: String, isSelected: Boolean) {
        Log.e("changeReaction", isSelected.toString())
        store.accept(ChatEvent.Ui.ChangeReaction(messageId, emojiName, isSelected))
    }

    private fun applyContentState(delegateList: List<DelegateItem>) {
        with(binding) {
            shimmer.hide()
            errorMessage.isVisible = false
            tryAgain.isVisible = false
            messages.isVisible = true
            messages.adapter = adapter
        }
        adapter.submitList(delegateList) {
            binding.messages.smoothScrollToPosition(adapter.itemCount - 1)
        }
    }

    private fun applyLoadingState() {
        with(binding) {
            shimmer.show()
            errorMessage.isVisible = false
            tryAgain.isVisible = false
            messages.isVisible = false
        }
    }

    private fun applyErrorLoadingState() {
        with(binding) {
            shimmer.hide()
            errorMessage.isVisible = true
            tryAgain.isVisible = true
            messages.isVisible = false
            errorMessage.text = getString(R.string.error_loading_messages)
        }
    }

    private fun applySendingError(delegates: List<DelegateItem>) {
        with(binding) {
            shimmer.hide()
            errorMessage.isVisible = false
            tryAgain.isVisible = false
            messages.isVisible = true
        }
        adapter.submitList(delegates) {
            binding.messages.smoothScrollToPosition(adapter.itemCount - 1)
        }
        showErrorSnackBar(getString(R.string.error_sending_message))
    }

    private fun applyChangeReactionError(delegates: List<DelegateItem>) {
        with(binding) {
            shimmer.hide()
            errorMessage.isVisible = false
            tryAgain.isVisible = false
            messages.isVisible = true
        }
        adapter.submitList(delegates) {
            binding.messages.smoothScrollToPosition(adapter.itemCount - 1)
        }
        showErrorSnackBar(getString(R.string.error_change_reaction))
    }

    private fun sendMessage() {
        val text = binding.messageInput.text.toString()
        if (text.trim().isNotEmpty()) {
            store.accept(ChatEvent.Ui.SendMessage(channelName, topicName, text))
            binding.messageInput.text?.clear()
        }
    }

    private fun changeButtonIcon(inputIsEmpty: Boolean) {
        val iconId = if (inputIsEmpty) {
            R.drawable.ic_upload
        } else {
            R.drawable.ic_send_message
        }

        binding.sendMessage.setImageResource(iconId)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun chooseReaction(messageId: Long) {
        val dialog = ChooseReactionDialogFragment.newInstance(messageId)
        dialog.show(requireActivity().supportFragmentManager, ChooseReactionDialogFragment.TAG)
        //TODO обработать выбор реакции
    }
}