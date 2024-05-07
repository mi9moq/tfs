package com.mironov.coursework.presentation.profile

import com.mironov.coursework.domain.entity.User

sealed interface ProfileEvent {

    sealed interface Ui : ProfileEvent {

        data object Initial : Ui

        data object LoadOwnProfile : Ui

        data class LoadUserById(val id: Int) : Ui

        data object OnArrowBackClicked : Ui
    }

    sealed interface Domain : ProfileEvent {

        data class LoadingSuccess(val user: User) : Domain

        data object LoadingFailure : Domain
    }
}