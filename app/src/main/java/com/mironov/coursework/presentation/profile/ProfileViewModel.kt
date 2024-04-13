package com.mironov.coursework.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mironov.coursework.data.mapper.toEntity
import com.mironov.coursework.data.network.api.ZulipApi
import com.mironov.coursework.navigation.router.ProfileRouter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class ProfileViewModel @Inject constructor(
    private val router: ProfileRouter,
    private val api: ZulipApi
) : ViewModel() {

    private val _state = MutableStateFlow<ProfileState>(ProfileState.Initial)
    val state = _state.asStateFlow()

    fun loadUser(userId: Int) {
        viewModelScope.launch {
            _state.value = ProfileState.Loading
            _state.value = ProfileState.Content(api.getUserById(userId).user.toEntity())
        }
    }

    fun loadOwnProfile() {
        viewModelScope.launch {
            _state.value = ProfileState.Loading
            _state.value = ProfileState.Content(api.getMyProfile().toEntity())
        }
    }

    fun back() {
        router.back()
    }
}