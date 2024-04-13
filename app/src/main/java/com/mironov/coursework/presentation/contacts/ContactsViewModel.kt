package com.mironov.coursework.presentation.contacts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mironov.coursework.data.mapper.toListEntity
import com.mironov.coursework.data.network.api.ZulipApi
import com.mironov.coursework.navigation.router.ContactsRouter
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class ContactsViewModel @Inject constructor(
    private val router: ContactsRouter,
    private val api: ZulipApi
) : ViewModel() {

    private val _state = MutableStateFlow<ContactsState>(ContactsState.Initial)
    val state = _state.asStateFlow()

    val searchQuery = MutableSharedFlow<String>(extraBufferCapacity = 1)

    init {
        //listenSearchQuery()
        viewModelScope.launch {
            _state.value = ContactsState.Loading
            val users = api.getAllUsersProfile().users.toListEntity()
            _state.value = ContactsState.Content(users)
        }
    }

//

//    private suspend fun search(query: String): ContactsState {
//        return viewModelScope.async {
//            val newList = contactList.filter {
//                it.userName.startsWith(query)
//            }.toList()
//            ContactsState.Content(newList)
//        }.await()
//    }

    fun openProfile(userId: Int) {
        router.openProfile(userId)
    }

    companion object {

        private const val SEARCH_DURATION_MILLIS = 500L
    }
}