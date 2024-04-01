package com.mironov.coursework.ui.contatcs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.mironov.coursework.databinding.FragmentContactsBinding
import com.mironov.coursework.domain.entity.User
import com.mironov.coursework.presentation.contacts.ContactsState
import com.mironov.coursework.presentation.contacts.ContactsViewModel
import com.mironov.coursework.ui.utils.collectStateFlow

class ContactsFragment : Fragment() {

    private var _binding: FragmentContactsBinding? = null
    private val binding: FragmentContactsBinding
        get() = _binding!!

    private val viewModel by viewModels<ContactsViewModel>()

    private val adapter by lazy {
        ContactsAdapter(viewModel::openProfile)
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
        observeViewModel()
    }

    private fun observeViewModel() {
        collectStateFlow(viewModel.state, ::applyState)
    }

    private fun applyState(state: ContactsState) {
        when (state) {
            is ContactsState.Content -> applyContentState(state.data)
            ContactsState.Initial -> Unit
        }
    }

    private fun applyContentState(contactList: List<User>) {
        binding.contacts.adapter = adapter
        adapter.submitList(contactList)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}