package com.mironov.coursework.ui.channels

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.mironov.coursework.R
import com.mironov.coursework.databinding.FragmentChannelsPageBinding
import com.mironov.coursework.presentation.ViewModelFactory
import com.mironov.coursework.presentation.channel.ChannelShareViewModel
import com.mironov.coursework.presentation.channel.ChannelState
import com.mironov.coursework.presentation.channel.ChannelViewModel
import com.mironov.coursework.presentation.channel.SharedChannelState
import com.mironov.coursework.ui.adapter.DelegateItem
import com.mironov.coursework.ui.adapter.MainAdapter
import com.mironov.coursework.ui.channels.chenal.ChannelDelegate
import com.mironov.coursework.ui.channels.topic.TopicDelegate
import com.mironov.coursework.ui.main.MainActivity
import com.mironov.coursework.ui.utils.collectStateFlow
import com.mironov.coursework.ui.utils.hide
import com.mironov.coursework.ui.utils.show
import javax.inject.Inject

class ChannelsPageFragment : Fragment() {

    companion object {

        fun newInstance(isAllChannels: Boolean) = ChannelsPageFragment().apply {
            arguments = Bundle().apply {
                putBoolean(IS_ALL_CHANNELS_KEY, isAllChannels)
            }
        }

        private const val IS_ALL_CHANNELS_KEY = "is all channels"
    }

    private var isAllChannels = false

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

    private val sharedViewModel by activityViewModels<ChannelShareViewModel>()

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseArguments()
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

        addClickListeners()
        observeState()
    }

    private fun addClickListeners() {
        binding.tryAgain.setOnClickListener {
            if (isAllChannels)
                viewModel.loadAllChannel()
            else
                viewModel.loadSubscribedChannel()
        }
    }

    private fun observeState() {
        collectStateFlow(viewModel.state, ::applyState)
        collectStateFlow(sharedViewModel.state, ::applySharedState)
    }

    private fun applyState(state: ChannelState) {
        when (state) {
            ChannelState.Initial -> Unit
            ChannelState.Loading -> applyLoadingState()
            ChannelState.Error -> applyErrorState()
            is ChannelState.Content -> applyContentState(state.data)
        }
    }

    private fun applyContentState(delegateItemList: List<DelegateItem>) {
        with(binding) {
            shimmer.hide()
            tryAgain.isVisible = false
            channels.isVisible = false
            channels.isVisible = true
            channels.adapter = adapter
        }
        adapter.submitList(delegateItemList)
    }

    private fun applyLoadingState() {
        with(binding) {
            errorMessage.isVisible = false
            tryAgain.isVisible = false
            channels.isVisible = false
            shimmer.show()
        }
    }

    private fun applyErrorState() {
        with(binding) {
            shimmer.hide()
            errorMessage.isVisible = true
            errorMessage.text = getString(R.string.error_loading)
            tryAgain.isVisible = true
            channels.isVisible = false
        }
    }

    private fun applySharedState(state: SharedChannelState) {
        when (state) {
            SharedChannelState.Initial -> Unit
            is SharedChannelState.Content -> {
                viewModel.filterChannels(state.data)
            }
        }
    }

    private fun parseArguments() {
        val args = requireArguments()
        isAllChannels = args.getBoolean(IS_ALL_CHANNELS_KEY)
        if (isAllChannels) viewModel.loadAllChannel() else viewModel.loadSubscribedChannel()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.channels.adapter = null
        _binding = null
    }
}