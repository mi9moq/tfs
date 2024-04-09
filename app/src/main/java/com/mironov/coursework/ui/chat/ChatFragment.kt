package com.mironov.coursework.ui.chat

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.mironov.coursework.R
import com.mironov.coursework.databinding.FragmentChatBinding
import com.mironov.coursework.presentation.ViewModelFactory
import com.mironov.coursework.presentation.chat.ChatState
import com.mironov.coursework.presentation.chat.ChatViewModel
import com.mironov.coursework.ui.adapter.DelegateItem
import com.mironov.coursework.ui.adapter.MainAdapter
import com.mironov.coursework.ui.main.MainActivity
import com.mironov.coursework.ui.chat.date.DateDelegate
import com.mironov.coursework.ui.chat.received.ReceivedDelegate
import com.mironov.coursework.ui.chat.sent.SentDelegate
import com.mironov.coursework.ui.reaction.ChooseReactionDialogFragment
import com.mironov.coursework.ui.utils.collectStateFlow
import com.mironov.coursework.ui.utils.hide
import com.mironov.coursework.ui.utils.show
import com.mironov.coursework.ui.utils.showErrorSnackBar
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
        initStatusBar()
        addClickListeners()
        addTextWatcher()
        observeViewModel()
    }

    private fun initStatusBar() {
        requireActivity().window.statusBarColor = requireContext()
            .getColor(R.color.primary_color)
    }

    private fun parseArguments() {
        val args = requireArguments()
        chatId = args.getInt(CHAT_ID_KEY)
    }

    private fun addTextWatcher() {
        binding.messageInput.doOnTextChanged { text, _, _, _ ->
            changeButtonIcon(text?.trim().isNullOrEmpty())
        }

        binding.tryAgain.setOnClickListener {
            viewModel.loadMessages(chatId)
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
        collectStateFlow(viewModel.state, ::applyState)
    }

    private fun applyState(state: ChatState) {
        when (state) {
            ChatState.Initial -> Unit
            ChatState.Loading -> applyLoadingState()

            is ChatState.Content -> applyContentState(state.data)
            ChatState.Error.LoadingError -> applyErrorLoadingState()

            is ChatState.Error.SendingError -> applyErrorSendingError(state.cache)
        }
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

    private fun applyErrorSendingError(delegates: List<DelegateItem>) {
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

    private fun sendMessage() {
        val text = binding.messageInput.text.toString()
        if (text.trim().isNotEmpty()) {
            viewModel.sendMessage(text)
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

    private fun chooseReaction(messageId: Int) {
        val dialog = ChooseReactionDialogFragment.newInstance(messageId)
        dialog.show(requireActivity().supportFragmentManager, ChooseReactionDialogFragment.TAG)
        dialog.onEmojiClickedCallback = viewModel::addReaction
    }
}