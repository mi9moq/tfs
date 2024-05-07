package com.mironov.coursework.navigation.router

import com.github.terrakok.cicerone.Router
import com.mironov.coursework.navigation.screen.getChatScreen
import javax.inject.Inject

interface ChannelRouter {

    fun openChat(channelName: String, topicName: String)
}

class ChannelRouterImpl @Inject constructor(
    private val router: Router
) : ChannelRouter {

    override fun openChat(channelName: String, topicName: String) {
        router.navigateTo(getChatScreen(channelName, topicName))
    }
}