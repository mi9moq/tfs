package com.mironov.coursework.ui.channel

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.mironov.coursework.R
import com.mironov.coursework.databinding.FragmentChannelsBinding

class ChannelsFragment : Fragment() {

    companion object {

        fun newInstance() = ChannelsFragment()

        private const val POSITION_SUBSCRIBE = 0
        private const val POSITION_ALL = 1
    }

    private var _binding: FragmentChannelsBinding? = null
    private val binding: FragmentChannelsBinding
        get() = _binding!!

    private lateinit var tabLayoutMediator: TabLayoutMediator
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
        setupViewPager()
    }

    private fun setupViewPager() {
        binding.viewPager.adapter = pagerAdapter
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

    override fun onDestroy() {
        super.onDestroy()
        tabLayoutMediator.detach()
        _binding = null
    }
}