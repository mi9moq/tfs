package com.mironov.coursework.ui.message

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.mironov.coursework.R
import com.mironov.coursework.databinding.FragmentChatBinding
import com.mironov.coursework.domain.entity.Message
import com.mironov.coursework.presentation.ViewModelFactory
import com.mironov.coursework.presentation.chat.ChatState
import com.mironov.coursework.presentation.chat.ChatViewModel
import com.mironov.coursework.ui.adapter.MainAdapter
import com.mironov.coursework.ui.main.MainActivity
import com.mironov.coursework.ui.message.date.DateDelegate
import com.mironov.coursework.ui.message.received.ReceivedDelegate
import com.mironov.coursework.ui.message.sent.SentDelegate
import com.mironov.coursework.ui.reaction.ChooseReactionDialogFragment
import com.mironov.coursework.ui.utils.collectStateFlow
import com.mironov.coursework.ui.utils.groupByDate
import com.mironov.coursework.ui.utils.hide
import com.mironov.coursework.ui.utils.show
import javax.inject.Inject

class ChatFragment : Fragment() {

    companion object {
        fun newInstance(chatId: Int) = ChatFragment().apply {
            arguments = Bundle().apply {
                putInt(CHAT_ID_KEY, chatId)
            }
        }

        private const val CHAT_ID_KEY = "chat id"
        private const val DEFAULT_ID = -1
    }

    private val component by lazy {
        (requireActivity() as MainActivity).component
    }

    private var chatId = DEFAULT_ID

    private var _binding: FragmentChatBinding? = null
    private val binding: FragmentChatBinding
        get() = _binding!!

    val adapter by lazy {
        MainAdapter().apply {
            addDelegate(DateDelegate())
            addDelegate(ReceivedDelegate(::chooseReaction, viewModel::changeReaction))
            addDelegate(SentDelegate(::chooseReaction, viewModel::changeReaction))
        }
    }

    private var messageId = 6

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel: ChatViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[ChatViewModel::class.java]
    }

    override fun onAttach(context: Context) {
        component.inject(this)
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseArguments()
        viewModel.loadMessages(chatId)
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

        addClickListeners()
        addTextWatcher()
        observeViewModel()
    }

    private fun parseArguments() {
        val args = requireArguments()
        chatId = args.getInt(CHAT_ID_KEY)
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
            viewModel.back()
        }
    }

    private fun observeViewModel() {
        collectStateFlow(viewModel.messages, ::applyState)
    }

    private fun applyState(state: ChatState) {
        when (state) {
            is ChatState.Content -> applyContentState(state.data)

            ChatState.Initial -> Unit

            ChatState.Loading -> applyLoadingState()
        }
    }

    private fun applyContentState(messages: List<Message>) {
        binding.messages.adapter = adapter
        binding.shimmer.hide()
        adapter.submitList(messages.groupByDate()) {
            binding.messages.smoothScrollToPosition(adapter.itemCount - 1)
        }
    }

    private fun applyLoadingState() {
        binding.shimmer.show()
    }

    private fun sendMessage() {
        val text = binding.messageInput.text.toString()
        if (viewModel.sendMessage(text, messageId)) {
            binding.messageInput.text?.clear()
            messageId++
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

    private fun chooseReaction(messageId: Int) {
        val dialog = ChooseReactionDialogFragment.newInstance(messageId)
        dialog.show(requireActivity().supportFragmentManager, ChooseReactionDialogFragment.TAG)
        dialog.onEmojiClickedCallback = viewModel::addReaction
    }
}