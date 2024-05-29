package com.mironov.coursework.navigation.router

import com.mironov.coursework.navigation.LocalCiceroneHolder
import com.mironov.coursework.navigation.screen.getChannelsScreen
import com.mironov.coursework.navigation.screen.getContactsScreen
import com.mironov.coursework.navigation.screen.getOwnProfileScreen
import com.mironov.coursework.ui.main.NavigationFragment.Companion.CICERONE_CONTAINER_TAG
import javax.inject.Inject

interface MainRouter {

    fun openChannels()

    fun openContacts()

    fun openOwnProfile()
}

class MainRouterImpl @Inject constructor(
    private val localCiceroneHolder: LocalCiceroneHolder
) : MainRouter {

    val router = localCiceroneHolder.getCicerone(CICERONE_CONTAINER_TAG).router

    override fun openChannels() {
        router.navigateTo(getChannelsScreen())
    }

    override fun openContacts() {
        router.navigateTo(getContactsScreen())
    }

    override fun openOwnProfile() {
        router.navigateTo(getOwnProfileScreen())
    }
}