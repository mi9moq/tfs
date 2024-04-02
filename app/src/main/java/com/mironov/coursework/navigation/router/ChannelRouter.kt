package com.mironov.coursework.navigation.router

import com.github.terrakok.cicerone.Router
import com.mironov.coursework.navigation.screen.getChatScreen
import javax.inject.Inject

interface ChannelRouter {

    fun openChat(chatId: Int)
}

class ChannelRouterImpl @Inject constructor(
    private val router: Router
) : ChannelRouter {

    override fun openChat(chatId: Int) {
        router.navigateTo(getChatScreen(chatId))
    }
}