package com.mironov.coursework.ui.channel

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.mironov.coursework.databinding.FragmentChannelsPageBinding
import com.mironov.coursework.presentation.channel.ChannelState
import com.mironov.coursework.presentation.channel.ChannelViewModel
import com.mironov.coursework.ui.message.adapter.DelegateItem
import com.mironov.coursework.ui.message.adapter.MainAdapter
import com.mironov.coursework.ui.utils.collectStateFlow

class ChannelsPageFragment : Fragment() {

    private var _binding: FragmentChannelsPageBinding? = null
    private val binding
        get() = _binding!!

    private val viewModel by viewModels<ChannelViewModel>()

    private val adapter by lazy {
        MainAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChannelsPageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter.addDelegate(ChannelDelegate())
        observeState()
    }

    private fun observeState() {
        collectStateFlow(viewModel.state, ::applyState)
    }

    private fun applyState(state: ChannelState) {
        when (state) {
            is ChannelState.Content -> applyContentState(state.data)
            ChannelState.Initial -> Unit
        }
    }

    private fun applyContentState(delegateItemList: List<DelegateItem>) {
        binding.channels.adapter = adapter
        adapter.submitList(delegateItemList)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}