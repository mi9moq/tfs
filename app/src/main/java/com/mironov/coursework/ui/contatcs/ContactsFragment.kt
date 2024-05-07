package com.mironov.coursework.ui.contatcs

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import com.mironov.coursework.R
import com.mironov.coursework.databinding.FragmentContactsBinding
import com.mironov.coursework.domain.entity.User
import com.mironov.coursework.presentation.contacts.ContactsCommand
import com.mironov.coursework.presentation.contacts.ContactsEffect
import com.mironov.coursework.presentation.contacts.ContactsEvent
import com.mironov.coursework.presentation.contacts.ContactsState
import com.mironov.coursework.ui.main.ElmBaseFragment
import com.mironov.coursework.ui.utils.appComponent
import com.mironov.coursework.ui.utils.hide
import com.mironov.coursework.ui.utils.show
import vivid.money.elmslie.android.renderer.elmStoreWithRenderer
import vivid.money.elmslie.core.store.ElmStore
import vivid.money.elmslie.core.store.Store
import javax.inject.Inject

class ContactsFragment : ElmBaseFragment<ContactsEffect, ContactsState, ContactsEvent>() {

    companion object {

        fun newInstance() = ContactsFragment()
    }

    override fun render(state: ContactsState) {
        if (state.isLoading) {
            applyLoadingState()
        }

        state.users?.let {
            applyContentState(it)
        }
    }

    @Inject
    lateinit var contactStore: ElmStore<ContactsEvent, ContactsState, ContactsEffect, ContactsCommand>

    override val store: Store<ContactsEvent, ContactsEffect, ContactsState> by elmStoreWithRenderer(
        elmRenderer = this
    ) {
        contactStore
    }

    private val component by lazy {
        requireContext().appComponent().geContactsComponentFactory().create()
    }

    private var _binding: FragmentContactsBinding? = null
    private val binding: FragmentContactsBinding
        get() = _binding!!

    private val adapter by lazy {
        ContactsAdapter(::openUserProfile)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        component.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentContactsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        store.accept(ContactsEvent.Ui.Initial)
        initStatusBar()
        addClickListeners()
        addTextChangeListener()
    }

    override fun handleEffect(effect: ContactsEffect): Unit = when (effect) {
        ContactsEffect.Error -> applyErrorState()
    }

    private fun initStatusBar() {
        requireActivity().window.statusBarColor = requireContext()
            .getColor(R.color.common_300)
    }

    private fun addTextChangeListener() {
        binding.search.doOnTextChanged { text, _, _, _ ->
            store.accept(ContactsEvent.Ui.ChangeFilter(text.toString()))
        }
    }

    private fun addClickListeners() {
        binding.tryAgain.setOnClickListener {
            store.accept(ContactsEvent.Ui.Refresh)
        }
    }

    private fun openUserProfile(id: Int) {
        store.accept(ContactsEvent.Ui.OpenUserProfile(id))
    }

    private fun applyContentState(contactList: List<User>) {
        binding.shimmer.hide()
        binding.contacts.isVisible = true
        binding.errorMessage.isVisible = false
        binding.tryAgain.isVisible = false
        binding.contacts.adapter = adapter
        adapter.submitList(contactList)
    }

    private fun applyLoadingState() {
        binding.shimmer.show()
        binding.contacts.isVisible = false
        binding.errorMessage.isVisible = false
        binding.tryAgain.isVisible = false
    }


    private fun applyErrorState() {
        binding.shimmer.hide()
        binding.contacts.isVisible = false
        binding.tryAgain.isVisible = true
        binding.errorMessage.isVisible = true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.contacts.adapter = null
        _binding = null
    }
}