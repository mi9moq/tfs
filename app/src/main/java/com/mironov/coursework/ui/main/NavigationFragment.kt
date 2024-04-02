package com.mironov.coursework.ui.main

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.github.terrakok.cicerone.Cicerone
import com.github.terrakok.cicerone.Router
import com.github.terrakok.cicerone.androidx.AppNavigator
import com.google.android.material.navigation.NavigationBarView.OnItemSelectedListener
import com.mironov.coursework.R
import com.mironov.coursework.databinding.FragmentNavigationBinding
import com.mironov.coursework.navigation.LocalCiceroneHolder
import com.mironov.coursework.presentation.ViewModelFactory
import com.mironov.coursework.presentation.main.NavigationViewModel
import javax.inject.Inject

class NavigationFragment : Fragment(), OnItemSelectedListener {

    companion object {
        fun newInstance() = NavigationFragment()
    }

    private val component by lazy {
        (requireActivity() as MainActivity).component
    }

    @Inject
    lateinit var localCiceroneHolder: LocalCiceroneHolder

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[NavigationViewModel::class.java]
    }

    private val cicerone: Cicerone<Router>
        get() = localCiceroneHolder.getCicerone("bottom navigation")

    private var _binding: FragmentNavigationBinding? = null
    private val binding: FragmentNavigationBinding
        get() = _binding!!

    override fun onAttach(context: Context) {
        component.inject(this)
        super.onAttach(context)
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
        cicerone.getNavigatorHolder().setNavigator(localNavigator)
    }

    override fun onPause() {
        cicerone.getNavigatorHolder().removeNavigator()
        super.onPause()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_channels -> {
//                cicerone.router.navigateTo(getChannelsScreen())
                viewModel.openChannels()
            }

            R.id.menu_people -> {
//                cicerone.router.navigateTo(getContactsScreen())
                viewModel.openContacts()
            }

            R.id.menu_profile -> {
//                cicerone.router.navigateTo(getOwnProfileScreen())
                viewModel.openOwnProfile()
            }
        }
        return true
    }
}