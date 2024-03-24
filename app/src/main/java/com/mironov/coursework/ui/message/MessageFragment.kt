package com.mironov.coursework.ui.message

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.mironov.coursework.R
import com.mironov.coursework.databinding.FragmentMessagesBinding
import com.mironov.coursework.presentation.message.MessageViewModel
import com.mironov.coursework.ui.message.adapter.MainAdapter
import com.mironov.coursework.ui.message.date.DateDelegate
import com.mironov.coursework.ui.message.received.ReceivedDelegate
import com.mironov.coursework.ui.message.sent.SentDelegate
import com.mironov.coursework.ui.utils.collectStateFlow
import com.mironov.coursework.ui.utils.groupByDate

class MessageFragment : Fragment() {

    private var _binding: FragmentMessagesBinding? = null
    private val binding: FragmentMessagesBinding
        get() = _binding!!

    val adapter by lazy {
        MainAdapter()
    }

    private val viewModel: MessageViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMessagesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter.apply {
            addDelegate(DateDelegate())
            addDelegate(ReceivedDelegate())
            addDelegate(SentDelegate())
        }
        addClickListeners()
        addTextWatcher()
        collectMessages()

        binding.messages.adapter = adapter
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
    }

    private fun collectMessages() {
        collectStateFlow(viewModel.messages){
            adapter.submitList(it.groupByDate())
        }
    }

    private fun sendMessage() {
        val text = binding.messageInput.text.toString()
        if (viewModel.sentMessage(text)){
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
}