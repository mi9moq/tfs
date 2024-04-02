package com.mironov.coursework.navigation.router

import com.github.terrakok.cicerone.Router
import javax.inject.Inject

interface ProfileRouter {

    fun back()
}

class ProfileRouterImpl @Inject constructor(
    private val router: Router
): ProfileRouter{

    override fun back() {
        router.exit()
    }
}