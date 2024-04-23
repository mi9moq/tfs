package com.mironov.coursework.ui.channels

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.mironov.coursework.R
import com.mironov.coursework.databinding.FragmentChannelsBinding
import com.mironov.coursework.presentation.channel.ChannelShareViewModel
import com.mironov.coursework.presentation.channel.QueryItem
import com.mironov.coursework.presentation.channel.SharedChannelState
import com.mironov.coursework.ui.utils.collectStateFlow

class ChannelsFragment : Fragment() {

    companion object {

        fun newInstance() = ChannelsFragment()

        private const val POSITION_SUBSCRIBE = 0
        private const val POSITION_ALL = 1
        private const val EMPTY_QUERY = ""
    }

    private var _binding: FragmentChannelsBinding? = null
    private val binding: FragmentChannelsBinding
        get() = _binding!!

    private val sharedViewModel by activityViewModels<ChannelShareViewModel>()

    private val queryAtPosition = arrayOf(EMPTY_QUERY, EMPTY_QUERY)

    private lateinit var tabLayoutMediator: TabLayoutMediator
    private lateinit var onPageChangeCallback: ViewPager2.OnPageChangeCallback

    private val pagerAdapter by lazy {
        ChannelsPagerAdapter(
            fragmentManager = childFragmentManager,
            lifecycle = lifecycle,
            items = listOf(
                ChannelsPageFragment.newInstance(false),
                ChannelsPageFragment.newInstance(true),
            )
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChannelsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initStatusBar()
        addTextChangeListener()
        observeViewModel()
        setupViewPager()
    }

    private fun initStatusBar() {
        requireActivity().window.statusBarColor = requireContext()
            .getColor(R.color.common_300)
    }

    private fun addTextChangeListener() {
        binding.search.doOnTextChanged { text, _, _, _ ->
            sharedViewModel.searchQuery.tryEmit(
                QueryItem(
                    text.toString(),
                    binding.viewPager.currentItem
                )
            )
        }
    }

    private fun observeViewModel() {
        collectStateFlow(sharedViewModel.state, ::applyState)
    }

    private fun applyState(state: SharedChannelState) {
        when (state) {
            SharedChannelState.Initial -> Unit
            is SharedChannelState.Content -> {
                queryAtPosition[state.data.screenPosition] = state.data.query
            }
        }
    }


    private fun setupViewPager() {
        onPageChangeCallback = object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                binding.search.setText(queryAtPosition[position])
            }
        }

        binding.viewPager.adapter = pagerAdapter
        binding.viewPager.registerOnPageChangeCallback(onPageChangeCallback)
        tabLayoutMediator = TabLayoutMediator(
            binding.tabLayout,
            binding.viewPager
        ) { tab, position ->
            when (position) {
                POSITION_SUBSCRIBE -> tab.setText(R.string.subscribed)
                POSITION_ALL -> tab.setText(R.string.all_streams)
                else -> throw IllegalStateException("Unknown position = $position")
            }
        }
        tabLayoutMediator.attach()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        tabLayoutMediator.detach()
        binding.viewPager.unregisterOnPageChangeCallback(onPageChangeCallback)
        binding.viewPager.adapter = null
        _binding = null
    }
}