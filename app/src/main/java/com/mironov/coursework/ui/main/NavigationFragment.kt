package com.mironov.coursework.ui.main

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.github.terrakok.cicerone.Cicerone
import com.github.terrakok.cicerone.NavigatorHolder
import com.github.terrakok.cicerone.Router
import com.github.terrakok.cicerone.androidx.AppNavigator
import com.google.android.material.navigation.NavigationBarView.OnItemSelectedListener
import com.mironov.coursework.R
import com.mironov.coursework.databinding.FragmentNavigationBinding
import com.mironov.coursework.navigation.screen.getChannelsScreen
import com.mironov.coursework.navigation.screen.getContactsScreen
import com.mironov.coursework.navigation.screen.getOwnProfileScreen

class NavigationFragment : Fragment(), OnItemSelectedListener {

    companion object {
        fun newInstance() = NavigationFragment()
    }

    private val component by lazy {
        (requireActivity() as MainActivity).component
    }

    private lateinit var navHolder: NavigatorHolder
    private lateinit var router: Router

    private var _binding: FragmentNavigationBinding? = null
    private val binding: FragmentNavigationBinding
        get() = _binding!!

    override fun onAttach(context: Context) {
        component.inject(this)
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val cicerone = Cicerone.create()
        navHolder = cicerone.getNavigatorHolder()
        router = cicerone.router
        if (savedInstanceState == null) {
            router.newRootScreen(getChannelsScreen())
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNavigationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.bottomNavigation.setOnItemSelectedListener(this)
    }

    override fun onResume() {
        super.onResume()
        val localNavigator = AppNavigator(requireActivity(), R.id.container, childFragmentManager)
        navHolder.setNavigator(localNavigator)
    }

    override fun onPause() {
        navHolder.removeNavigator()
        super.onPause()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_channels -> {
                router.navigateTo(getChannelsScreen())
            }

            R.id.menu_people -> {
                router.navigateTo(getContactsScreen())
            }

            R.id.menu_profile -> {
                router.navigateTo(getOwnProfileScreen())
            }
        }
        return true
    }
}