package com.mironov.coursework.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mironov.coursework.domain.entity.User
import com.mironov.coursework.navigation.router.ProfileRouter
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random

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
        viewModelScope.launch {
            _state.value = ProfileState.Loading
            delay(1000)
            if (Random.nextBoolean()){
                _state.value = ProfileState.Content(user)
            } else {
                _state.value = ProfileState.Error
            }
        }
    }

    fun back() {
        router.back()
    }
}