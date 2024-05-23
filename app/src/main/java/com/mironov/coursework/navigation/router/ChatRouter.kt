package com.mironov.coursework.navigation.router

import com.github.terrakok.cicerone.Router
import com.mironov.coursework.navigation.screen.getChatScreen
import javax.inject.Inject

interface ChatRouter {

    fun back()

    fun showTopic(channelName: String, topicName: String)
}

class ChatRouterImpl @Inject constructor(
    private val router: Router
): ChatRouter {

    override fun back() {
        router.exit()
    }

    override fun showTopic(channelName: String, topicName: String) {
        router.navigateTo(getChatScreen(channelName, topicName))
    }
}