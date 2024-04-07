package com.mironov.coursework.presentation.contacts

import com.mironov.coursework.domain.entity.User

sealed interface ContactsState {

    data object Initial : ContactsState

    data object Loading : ContactsState

    data object Error : ContactsState

    data class Content(val data: List<User>) : ContactsState
}