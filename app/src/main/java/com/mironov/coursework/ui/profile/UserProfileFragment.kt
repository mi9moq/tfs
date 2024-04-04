package com.mironov.coursework.ui.profile

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.mironov.coursework.R
import com.mironov.coursework.databinding.FragmentProfileBinding
import com.mironov.coursework.domain.entity.User
import com.mironov.coursework.presentation.ViewModelFactory
import com.mironov.coursework.presentation.profile.ProfileState
import com.mironov.coursework.presentation.profile.ProfileViewModel
import com.mironov.coursework.ui.main.MainActivity
import com.mironov.coursework.ui.utils.collectStateFlow
import com.mironov.coursework.ui.utils.hide
import com.mironov.coursework.ui.utils.show
import javax.inject.Inject

class UserProfileFragment : Fragment() {

    companion object {
        fun newInstance(userId: Int) = UserProfileFragment().apply {
            arguments = Bundle().apply {
                putInt(ID_KEY, userId)
            }
        }

        private const val ID_KEY = "user id"
        private const val DEFAULT_ID = -1
    }

    private val component by lazy {
        (requireActivity() as MainActivity).component
    }

    private var id = DEFAULT_ID

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
        binding.logOut.isVisible = false
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
            ProfileState.Loading -> applyLoadingState()
        }
    }

    private fun applyContentState(user: User) {
        with(binding) {
            shimmer.hide()
            avatar.setImageResource(R.drawable.ic_avatar) //TODO переписать на coil
            userName.text = user.userName
            status.text = user.status
            online.text = if (user.isOnline)
                getString(R.string.online)
            else
                getString(R.string.offline)
            toolbar.isVisible = true
            toolbar.setNavigationOnClickListener {
                viewModel.back()
            }
        }
    }

    private fun applyLoadingState() {
        binding.shimmer.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}