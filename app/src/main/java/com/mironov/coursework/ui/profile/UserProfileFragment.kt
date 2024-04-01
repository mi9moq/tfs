package com.mironov.coursework.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.mironov.coursework.R
import com.mironov.coursework.databinding.FragmentProfileBinding
import com.mironov.coursework.domain.entity.User
import com.mironov.coursework.presentation.profile.ProfileState
import com.mironov.coursework.presentation.profile.ProfileViewModel
import com.mironov.coursework.ui.utils.collectStateFlow

class UserProfileFragment : Fragment() {

    companion object {
        fun newInstance(userId: Int) = UserProfileFragment().apply {
            Bundle().apply {
                putInt(ID_KEY, userId)
            }
        }

        private const val ID_KEY = "user id"
        private const val DEFAULT_ID = -1
    }

    private var id = DEFAULT_ID

    private var _binding: FragmentProfileBinding? = null
    private val binding: FragmentProfileBinding
        get() = _binding!!

    private val viewModel by viewModels<ProfileViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseArguments()
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
        observeState()
    }

    private fun parseArguments() {
        val args = requireArguments()
        id = args.getInt(ID_KEY)
        viewModel.loadUser(id)
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
            logOut.isVisible = false
            avatar.setImageResource(R.drawable.ic_avatar) //TODO переписать на coil
            userName.text = user.userName
            status.text = user.status
            online.text = if (user.isOnline) "online" else "offline"
            toolbar.isVisible = true
            toolbar.setNavigationOnClickListener {
                viewModel.back()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}