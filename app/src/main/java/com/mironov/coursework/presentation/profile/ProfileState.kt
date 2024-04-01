package com.mironov.coursework.presentation.profile

import com.mironov.coursework.domain.entity.User

sealed interface ProfileState {

    data object Initial: ProfileState

    data class Content(val data: User): ProfileState
}