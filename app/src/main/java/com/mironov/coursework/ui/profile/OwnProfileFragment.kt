package com.mironov.coursework.ui.profile

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import coil.load
import coil.transform.RoundedCornersTransformation
import com.mironov.coursework.R
import com.mironov.coursework.databinding.FragmentProfileBinding
import com.mironov.coursework.domain.entity.User
import com.mironov.coursework.presentation.profile.ProfileCommand
import com.mironov.coursework.presentation.profile.ProfileEffect
import com.mironov.coursework.presentation.profile.ProfileEvent
import com.mironov.coursework.presentation.profile.ProfileState
import com.mironov.coursework.ui.main.ElmBaseFragment
import com.mironov.coursework.ui.main.MainActivity
import com.mironov.coursework.ui.utils.applyPresence
import com.mironov.coursework.ui.utils.hide
import com.mironov.coursework.ui.utils.show
import vivid.money.elmslie.android.renderer.elmStoreWithRenderer
import vivid.money.elmslie.core.store.ElmStore
import vivid.money.elmslie.core.store.Store
import javax.inject.Inject

class OwnProfileFragment : ElmBaseFragment<ProfileEffect, ProfileState, ProfileEvent>() {

    companion object {
        fun newInstance() = OwnProfileFragment()
    }

    private val component by lazy {
        (requireActivity() as MainActivity).component.geProfileComponentFactory().create()
    }

    private var _binding: FragmentProfileBinding? = null
    private val binding: FragmentProfileBinding
        get() = _binding!!

    @Inject
    lateinit var profileStore: ElmStore<ProfileEvent, ProfileState, ProfileEffect, ProfileCommand>

    override val store: Store<ProfileEvent, ProfileEffect, ProfileState> by elmStoreWithRenderer(
        elmRenderer = this
    ) {
        profileStore
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
        store.accept(ProfileEvent.Ui.LoadOwnProfile)
        initStatusBar()
        addClickListeners()
    }

    override fun handleEffect(effect: ProfileEffect): Unit = when (effect) {
        ProfileEffect.Error -> applyErrorState()
    }

    override fun render(state: ProfileState) {
        if (state.isLoading) {
            applyLoadingState()
        }

        state.user?.let {
            applyContentState(it)
        }
    }

    private fun initStatusBar() {
        binding.toolbar.visibility = View.GONE
        requireActivity().window.statusBarColor = requireContext()
            .getColor(R.color.common_300)
    }

    private fun addClickListeners() {
        binding.tryAgain.setOnClickListener {
            store.accept(ProfileEvent.Ui.LoadOwnProfile)
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
            toolbar.visibility = View.GONE
            errorMessage.isVisible = false
            tryAgain.isVisible = false
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