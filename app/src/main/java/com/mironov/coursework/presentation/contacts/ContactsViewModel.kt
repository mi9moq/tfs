package com.mironov.coursework.presentation.contacts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mironov.coursework.domain.entity.User
import com.mironov.coursework.navigation.router.ContactsRouter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

class ContactsViewModel @Inject constructor(
    private val router: ContactsRouter
) : ViewModel() {

    private val contactList = listOf(
        User(
            id = 1,
            userName = "User 1",
            email = "email1@mail.com",
            avatarUrl = "",
            isOnline = false,
            status = ""
        ),
        User(
            id = 2,
            userName = "User 2",
            email = "email2@mail.com",
            avatarUrl = "",
            isOnline = false,
            status = ""
        ),
        User(
            id = 3,
            userName = "User 3",
            email = "email3@mail.com",
            avatarUrl = "",
            isOnline = false,
            status = ""
        ),
        User(
            id = 4,
            userName = "User 4",
            email = "email4@mail.com",
            avatarUrl = "",
            isOnline = false,
            status = ""
        ),
    )

    private val _state = MutableStateFlow<ContactsState>(ContactsState.Initial)
    val state = _state.asStateFlow()

    val searchQuery = MutableSharedFlow<String>(extraBufferCapacity = 1)

    init {
        listenSearchQuery()
        viewModelScope.launch {
            _state.value = ContactsState.Loading
            delay(1000)
            _state.value = ContactsState.Content(contactList)
        }
    }

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    private fun listenSearchQuery() {
        searchQuery
            .filter { it.isNotEmpty() }
            .distinctUntilChanged()
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
            val newList = contactList.filter {
                it.userName.startsWith(query)
            }.toList()
            ContactsState.Content(newList)
        }.await()
    }

    fun openProfile(userId: Int) {
        router.openProfile(userId)
    }

    companion object {

        private const val SEARCH_DURATION_MILLIS = 500L
    }
}