package com.mironov.coursework.presentation.profile

import com.mironov.coursework.domain.entity.User

sealed interface ProfileState {

    data object Initial: ProfileState

    data object Loading: ProfileState

    data object Error: ProfileState

    data class Content(val data: User): ProfileState
}