package com.mironov.coursework.ui.channels

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import com.mironov.coursework.R
import com.mironov.coursework.databinding.FragmentChannelsPageBinding
import com.mironov.coursework.domain.entity.Channel
import com.mironov.coursework.domain.entity.Topic
import com.mironov.coursework.presentation.channel.ChannelEffect
import com.mironov.coursework.presentation.channel.ChannelEvent
import com.mironov.coursework.presentation.channel.ChannelShareViewModel
import com.mironov.coursework.presentation.channel.ChannelState
import com.mironov.coursework.presentation.channel.ChannelStoreFactory
import com.mironov.coursework.presentation.channel.SharedChannelState
import com.mironov.coursework.ui.adapter.DelegateItem
import com.mironov.coursework.ui.adapter.MainAdapter
import com.mironov.coursework.ui.channels.chenal.ChannelDelegate
import com.mironov.coursework.ui.channels.topic.TopicDelegate
import com.mironov.coursework.ui.main.ElmBaseFragment
import com.mironov.coursework.ui.main.MainActivity
import com.mironov.coursework.ui.utils.collectStateFlow
import com.mironov.coursework.ui.utils.hide
import com.mironov.coursework.ui.utils.show
import vivid.money.elmslie.android.renderer.elmStoreWithRenderer
import vivid.money.elmslie.core.store.Store
import javax.inject.Inject

class ChannelsPageFragment : ElmBaseFragment<ChannelEffect, ChannelState, ChannelEvent>() {

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
    lateinit var channelStoreFactory: ChannelStoreFactory

    override val store: Store<ChannelEvent, ChannelEffect, ChannelState> by elmStoreWithRenderer(
        elmRenderer = this
    ) {
        channelStoreFactory.create()
    }

    private val sharedViewModel by activityViewModels<ChannelShareViewModel>()

    private val adapter by lazy {
        MainAdapter().apply {
            addDelegate(
                ChannelDelegate(
                    ::showTopics,
                    ::hideTopics
                )
            )
            addDelegate(TopicDelegate(::onTopicClicked))
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
        applyInitialState()
        addClickListeners()
        observeState()
    }

    override fun render(state: ChannelState) {
        if (state.isLoading) {
            applyLoadingState()
        }

        state.content?.let {
            applyContentState(it)
        }
    }

    override fun handleEffect(effect: ChannelEffect): Unit = when (effect) {
        ChannelEffect.ErrorLoadingChannels -> applyErrorState()
        ChannelEffect.ErrorLoadingTopics -> applyErrorState()
    }

    private fun addClickListeners() {
        binding.tryAgain.setOnClickListener {
            if (isAllChannels)
                store.accept(ChannelEvent.Ui.InitialAll)
            else
                store.accept(ChannelEvent.Ui.InitialSubscribed)
        }
    }

    private fun observeState() {
        collectStateFlow(sharedViewModel.state, ::applySharedState)
    }

    private fun applyInitialState() {
        if (isAllChannels)
            store.accept(ChannelEvent.Ui.InitialAll)
        else
            store.accept(ChannelEvent.Ui.InitialSubscribed)
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
                store.accept(ChannelEvent.Ui.ChangeFilter(state.data))
            }
        }
    }

    private fun showTopics(channel: Channel) {
        store.accept(ChannelEvent.Ui.ShowTopic(channel))
    }

    private fun hideTopics(channelId: Int) {
        store.accept(ChannelEvent.Ui.HideTopic(channelId))
    }

    fun onTopicClicked(topic: Topic) {
        store.accept(ChannelEvent.Ui.OnTopicClicked(topic))
    }

    private fun parseArguments() {
        val args = requireArguments()
        isAllChannels = args.getBoolean(IS_ALL_CHANNELS_KEY)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.channels.adapter = null
        _binding = null
    }
}