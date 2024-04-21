package com.mironov.coursework.presentation.contacts

import com.mironov.coursework.domain.entity.User

sealed interface ContactsEvent {

    sealed interface Ui : ContactsEvent {

        data object Initial : Ui

        data object Refresh : Ui

        data class OpenUserProfile(val id: Int) : Ui
    }

    sealed interface Domain : ContactsEvent {

        data class LoadingSuccess(val users: List<User>) : Domain

        data object LoadingFailure : Domain
    }
}