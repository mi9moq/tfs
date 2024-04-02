package com.mironov.coursework.ui.channels

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.mironov.coursework.databinding.FragmentChannelsPageBinding
import com.mironov.coursework.presentation.ViewModelFactory
import com.mironov.coursework.presentation.channel.ChannelState
import com.mironov.coursework.presentation.channel.ChannelViewModel
import com.mironov.coursework.ui.main.MainActivity
import com.mironov.coursework.ui.adapter.DelegateItem
import com.mironov.coursework.ui.adapter.MainAdapter
import com.mironov.coursework.ui.channels.chenal.ChannelDelegate
import com.mironov.coursework.ui.channels.topic.TopicDelegate
import com.mironov.coursework.ui.utils.collectStateFlow
import javax.inject.Inject

class ChannelsPageFragment : Fragment() {

    companion object {

        fun newInstance(isAllChannels: Boolean) = ChannelsPageFragment().apply {
            Bundle().apply {
                putBoolean(IS_ALL_CHANNELS_KEY, isAllChannels)
            }
        }

        private const val IS_ALL_CHANNELS_KEY = "is all channels"
    }

    private val component by lazy {
        (requireActivity() as MainActivity).component
    }

    private var _binding: FragmentChannelsPageBinding? = null
    private val binding
        get() = _binding!!

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[ChannelViewModel::class.java]
    }

    private val adapter by lazy {
        MainAdapter().apply {
            addDelegate(
                ChannelDelegate(
                    viewModel::showTopics,
                    viewModel::hideTopics
                )
            )
            addDelegate(TopicDelegate(viewModel::openChat))
        }
    }

    override fun onAttach(context: Context) {
        component.inject(this)
        super.onAttach(context)
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