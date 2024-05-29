package com.mironov.coursework.navigation.router

import com.github.terrakok.cicerone.Router
import com.mironov.coursework.navigation.screen.getProfileScreen
import javax.inject.Inject

interface ContactsRouter {

    fun openProfile(userId: Int)
}

class ContactsRouterImpl @Inject constructor(
    private val router: Router
): ContactsRouter{

    override fun openProfile(userId: Int) {
        router.navigateTo(getProfileScreen(userId))
    }
}