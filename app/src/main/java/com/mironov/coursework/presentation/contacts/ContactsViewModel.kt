package com.mironov.coursework.presentation.contacts

import androidx.lifecycle.ViewModel
import com.mironov.coursework.domain.entity.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class ContactsViewModel : ViewModel() {

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

    init {
        _state.value = ContactsState.Content(contactList)
    }

    fun openProfile(userId: Int) {
        //TODO navigate to profile
    }
}