package com.mironov.coursework.navigation.router

import com.github.terrakok.cicerone.Router
import com.mironov.coursework.navigation.screen.getChatScreen
import com.mironov.coursework.presentation.chat.ChatInfo
import javax.inject.Inject

interface ChatRouter {

    fun back()

    fun showTopic(chatInfo: ChatInfo)
}

class ChatRouterImpl @Inject constructor(
    private val router: Router
): ChatRouter {

    override fun back() {
        router.exit()
    }

    override fun showTopic(chatInfo: ChatInfo) {
        router.navigateTo(getChatScreen(chatInfo))
    }
}