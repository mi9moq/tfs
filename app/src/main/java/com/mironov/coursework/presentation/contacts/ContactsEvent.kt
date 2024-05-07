package com.mironov.coursework.presentation.contacts

import com.mironov.coursework.domain.entity.User

sealed interface ContactsEvent {

    sealed interface Ui : ContactsEvent {

        data object Initial : Ui

        data object Refresh : Ui

        data class OpenUserProfile(val id: Int) : Ui

        data class ChangeFilter(val query: String) : Ui
    }

    sealed interface Domain : ContactsEvent {

        data class LoadingSuccess(val users: List<User>) : Domain

        data object LoadingFailure : Domain

        data class FilterSuccess(val users: List<User>) : Domain
    }
}