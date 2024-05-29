package com.mironov.coursework.navigation.router

import com.github.terrakok.cicerone.Router
import com.mironov.coursework.navigation.screen.getChatScreen
import com.mironov.coursework.presentation.chat.ChatInfo
import javax.inject.Inject

interface ChannelRouter {

    fun openChat(chatInfo: ChatInfo)
}

class ChannelRouterImpl @Inject constructor(
    private val router: Router
) : ChannelRouter {

    override fun openChat(chatInfo: ChatInfo) {
        router.navigateTo(getChatScreen(chatInfo))
    }
}