package com.mironov.coursework.presentation.contacts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mironov.coursework.domain.entity.User
import com.mironov.coursework.domain.repository.Result
import com.mironov.coursework.domain.usecase.GetAllUsersUseCase
import com.mironov.coursework.navigation.router.ContactsRouter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

class ContactsViewModel @Inject constructor(
    private val router: ContactsRouter,
    private val getAllUsersUseCase: GetAllUsersUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow<ContactsState>(ContactsState.Initial)
    val state = _state.asStateFlow()

    val searchQuery = MutableSharedFlow<String>(extraBufferCapacity = 1)
    private val cache = mutableListOf<User>()

    init {
        loadContacts()
        listenSearchQuery()
    }

    fun loadContacts() {
        viewModelScope.launch {
            _state.value = ContactsState.Loading
            cache.clear()
            when (val result = getAllUsersUseCase()) {
                is Result.Success -> {
                    cache.addAll(result.content)
                    _state.value = ContactsState.Content(result.content)
                }

                is Result.Failure -> _state.value = ContactsState.Error
            }
        }
    }

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    private fun listenSearchQuery() {
        searchQuery
            .debounce(SEARCH_DURATION_MILLIS)
            .mapLatest(::search)
            .flowOn(Dispatchers.Default)
            .onEach {
                _state.value = it
            }
            .launchIn(viewModelScope)
    }


    private suspend fun search(query: String): ContactsState {
        return viewModelScope.async {
            if (_state.value !is ContactsState.Content) {
                _state.value
            } else if (query.isBlank()) {
                ContactsState.Content(cache)
            } else {
                val newList = cache.filter {
                    it.userName.startsWith(query)
                }.toList()
                ContactsState.Content(newList)
            }
        }.await()
    }

    fun openProfile(userId: Int) {
        router.openProfile(userId)
    }

    companion object {

        private const val SEARCH_DURATION_MILLIS = 500L
    }
}