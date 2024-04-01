package com.mironov.coursework.presentation.contacts

import com.mironov.coursework.domain.entity.User

sealed interface ContactsState {

    data object Initial : ContactsState

    data class Content(val data: List<User>) : ContactsState
}