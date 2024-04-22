package com.mironov.coursework.presentation.profile

sealed interface ProfileCommand {

    data object LoadOwnProfile: ProfileCommand

    data class LoadProfileById(val id: Int): ProfileCommand
}