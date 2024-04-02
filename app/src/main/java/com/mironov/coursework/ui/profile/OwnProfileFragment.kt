package com.mironov.coursework.ui.profile

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.mironov.coursework.R
import com.mironov.coursework.databinding.FragmentProfileBinding
import com.mironov.coursework.domain.entity.User
import com.mironov.coursework.presentation.ViewModelFactory
import com.mironov.coursework.presentation.contacts.ContactsViewModel
import com.mironov.coursework.presentation.profile.ProfileState
import com.mironov.coursework.presentation.profile.ProfileViewModel
import com.mironov.coursework.ui.contatcs.ContactsAdapter
import com.mironov.coursework.ui.main.MainActivity
import com.mironov.coursework.ui.utils.collectStateFlow
import javax.inject.Inject

class OwnProfileFragment : Fragment() {

    companion object {
        fun newInstance() = OwnProfileFragment()
    }

    private val component by lazy {
        (requireActivity() as MainActivity).component
    }

    private var _binding: FragmentProfileBinding? = null
    private val binding: FragmentProfileBinding
        get() = _binding!!

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[ProfileViewModel::class.java]
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
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.loadUser(1)
        observeState()
    }

    private fun observeState() {
        collectStateFlow(viewModel.state, ::applyState)
    }

    private fun applyState(state: ProfileState) {
        when (state) {
            is ProfileState.Content -> applyContentState(state.data)
            ProfileState.Initial -> Unit
        }
    }

    private fun applyContentState(user: User) {
        with(binding) {
            logOut.isVisible = true
            avatar.setImageResource(R.drawable.ic_avatar) //TODO переписать на coil
            userName.text = user.userName
            status.text = user.status
            online.text = getString(R.string.online)
            toolbar.visibility = View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}