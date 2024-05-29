package com.mironov.coursework.presentation.profile

import com.mironov.coursework.domain.entity.User

data class ProfileState(
    val isLoading: Boolean = false,
    val user: User? = null
)
