package com.mironov.coursework.navigation.router

import com.github.terrakok.cicerone.Router
import javax.inject.Inject

interface ChatRouter {

    fun back()
}

class ChatRouterImpl @Inject constructor(
    private val router: Router
): ChatRouter {

    override fun back() {
        router.exit()
    }
}