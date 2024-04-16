package com.mironov.coursework.ui.contatcs

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.mironov.coursework.R
import com.mironov.coursework.databinding.FragmentContactsBinding
import com.mironov.coursework.domain.entity.User
import com.mironov.coursework.presentation.ViewModelFactory
import com.mironov.coursework.presentation.contacts.ContactsState
import com.mironov.coursework.presentation.contacts.ContactsViewModel
import com.mironov.coursework.ui.main.MainActivity
import com.mironov.coursework.ui.utils.collectStateFlow
import com.mironov.coursework.ui.utils.hide
import com.mironov.coursework.ui.utils.show
import javax.inject.Inject

class ContactsFragment : Fragment() {

    companion object {

        fun newInstance() = ContactsFragment()
    }

    private val component by lazy {
        (requireActivity() as MainActivity).component
    }

    private var _binding: FragmentContactsBinding? = null
    private val binding: FragmentContactsBinding
        get() = _binding!!

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[ContactsViewModel::class.java]
    }

    private val adapter by lazy {
        ContactsAdapter(viewModel::openProfile)
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
        initStatusBar()
        addTextChangeListener()
        addClickListeners()
        observeViewModel()
    }

    private fun initStatusBar() {
        requireActivity().window.statusBarColor = requireContext()
            .getColor(R.color.common_300)
    }

    private fun addTextChangeListener() {
        binding.search.doOnTextChanged { text, _, _, _ ->
            viewModel.searchQuery.tryEmit(text.toString())
        }
    }

    private fun addClickListeners() {
        binding.tryAgain.setOnClickListener {
            viewModel.loadContacts()
        }
    }

    private fun observeViewModel() {
        collectStateFlow(viewModel.state, ::applyState)
    }

    private fun applyState(state: ContactsState) {
        when (state) {
            ContactsState.Initial -> Unit
            ContactsState.Loading -> applyLoadingState()
            ContactsState.Error -> applyErrorState()
            is ContactsState.Content -> applyContentState(state.data)
        }
    }

    private fun applyContentState(contactList: List<User>) {
        binding.shimmer.hide()
        binding.contacts.isVisible = true
        binding.contacts.isVisible = false
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
        binding.tryAgain.isVisible = true
        binding.errorMessage.isVisible = true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.contacts.adapter = null
        _binding = null
    }
}