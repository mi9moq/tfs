package com.mironov.coursework.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mironov.coursework.domain.repository.Result
import com.mironov.coursework.domain.usecase.GetOwnProfileUseCase
import com.mironov.coursework.domain.usecase.GetUserByIdUseCase
import com.mironov.coursework.navigation.router.ProfileRouter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class ProfileViewModel @Inject constructor(
    private val router: ProfileRouter,
    private val getUserByIdUseCase: GetUserByIdUseCase,
    private val getOwnProfileUseCase: GetOwnProfileUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow<ProfileState>(ProfileState.Initial)
    val state = _state.asStateFlow()

    fun loadUser(userId: Int) {
        viewModelScope.launch {
            _state.value = ProfileState.Loading
            when (val result = getUserByIdUseCase(userId)) {
                is Result.Success -> _state.value = ProfileState.Content(result.content)
                is Result.Failure -> _state.value = ProfileState.Error
            }
        }
    }

    fun loadOwnProfile() {
        viewModelScope.launch {
            _state.value = ProfileState.Loading
            when (val result = getOwnProfileUseCase()) {
                is Result.Success -> _state.value = ProfileState.Content(result.content)
                is Result.Failure -> _state.value = ProfileState.Error
            }
        }
    }

    fun back() {
        router.back()
    }
}