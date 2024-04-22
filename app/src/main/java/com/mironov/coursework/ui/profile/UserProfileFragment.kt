package com.mironov.coursework.ui.profile

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import coil.load
import coil.transform.RoundedCornersTransformation
import com.mironov.coursework.ui.main.ElmBaseFragment
import com.mironov.coursework.R
import com.mironov.coursework.databinding.FragmentProfileBinding
import com.mironov.coursework.domain.entity.User
import com.mironov.coursework.presentation.profile.ProfileEffect
import com.mironov.coursework.presentation.profile.ProfileEvent
import com.mironov.coursework.presentation.profile.ProfileState
import com.mironov.coursework.presentation.profile.ProfileStoreFactory
import com.mironov.coursework.ui.main.MainActivity
import com.mironov.coursework.ui.utils.applyPresence
import com.mironov.coursework.ui.utils.hide
import com.mironov.coursework.ui.utils.show
import vivid.money.elmslie.android.renderer.elmStoreWithRenderer
import vivid.money.elmslie.core.store.Store
import javax.inject.Inject

class UserProfileFragment : ElmBaseFragment<ProfileEffect, ProfileState, ProfileEvent>() {

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
    lateinit var profileStoreFactory: ProfileStoreFactory

    override val store: Store<ProfileEvent, ProfileEffect, ProfileState> by elmStoreWithRenderer(
        elmRenderer = this
    ) {
        profileStoreFactory.create()
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
        store.accept(ProfileEvent.Ui.LoadUserById(id))
        initStatusBar()
        addClickListeners()
    }

    override fun render(state: ProfileState) {
        if (state.isLoading)
            applyLoadingState()

        state.user?.let {
            applyContentState(it)
        }
    }

    override fun handleEffect(effect: ProfileEffect): Unit = when (effect) {
        ProfileEffect.Error -> applyErrorState()
    }

    private fun parseArguments() {
        val args = requireArguments()
        id = args.getInt(ID_KEY)
    }

    private fun initStatusBar() {
        requireActivity().window.statusBarColor = requireContext()
            .getColor(R.color.common_300)
        binding.toolbar.setNavigationOnClickListener {
            store.accept(ProfileEvent.Ui.OnArrowBackClicked)
        }
    }

    private fun addClickListeners() {
        binding.tryAgain.setOnClickListener {
            store.accept(ProfileEvent.Ui.LoadUserById(id))
        }
    }

    private fun applyContentState(user: User) {
        with(binding) {
            shimmer.hide()
            avatar.load(user.avatarUrl) {
                transformations(RoundedCornersTransformation(16f))
                error(R.drawable.ic_avatar)
            }
            userName.text = user.userName
            presence.applyPresence(user.presence, requireContext())
            toolbar.isVisible = true
        }
    }

    private fun applyLoadingState() {
        with(binding) {
            shimmer.show()
            errorMessage.isVisible = false
            tryAgain.isVisible = false
        }
    }

    private fun applyErrorState() {
        with(binding) {
            shimmer.hide()
            errorMessage.isVisible = true
            tryAgain.isVisible = true
            errorMessage.setText(R.string.error_loading_profile)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}