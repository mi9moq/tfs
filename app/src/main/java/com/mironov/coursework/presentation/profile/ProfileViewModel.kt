package com.mironov.coursework.presentation.profile

import androidx.lifecycle.ViewModel
import com.mironov.coursework.domain.entity.User
import com.mironov.coursework.navigation.router.ProfileRouter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

class ProfileViewModel @Inject constructor(
    private val router: ProfileRouter
): ViewModel() {

    private val user = User(
        id = 1,
        userName = "User Name",
        email = "email@mail.ru",
        avatarUrl = "",
        isOnline = true,
        status = "In a meeting"
    )

    private val _state = MutableStateFlow<ProfileState>(ProfileState.Initial)
    val state = _state.asStateFlow()

    fun loadUser(userId: Int) {
        _state.value = ProfileState.Content(user)
    }

    fun back() {
        router.back()
    }
}