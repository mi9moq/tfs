package com.mironov.coursework.presentation.main

import androidx.lifecycle.ViewModel
import com.mironov.coursework.navigation.router.MainRouter
import javax.inject.Inject

class NavigationViewModel @Inject constructor(
    private val router: MainRouter
) : ViewModel() {

    fun openChannels() {
        router.openChannels()
    }

    fun openContacts() {
        router.openContacts()
    }

    fun openOwnProfile() {
        router.openOwnProfile()
    }
}