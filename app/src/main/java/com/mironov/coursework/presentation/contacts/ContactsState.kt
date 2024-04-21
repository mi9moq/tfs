package com.mironov.coursework.presentation.contacts

import com.mironov.coursework.domain.entity.User

data class ContactsState(
    val isLoading: Boolean = false,
    val users: List<User>? = null,
)