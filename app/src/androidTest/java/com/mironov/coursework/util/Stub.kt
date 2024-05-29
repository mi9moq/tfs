package com.mironov.coursework.util

import com.mironov.coursework.presentation.chat.ChatInfo
import com.mironov.coursework.ui.utils.toFormatDate
import com.mironov.coursework.ui.utils.toLocalDate

val firstMessageDate = 1713230866L.toLocalDate().toFormatDate()

const val CHANNEL_NAME = "Test Channel"
const val TOPIC_NAME = "Test topic"
const val CHAT_INFO_KEY = "chat info"

val chatInfoWithTopic = ChatInfo(
    topicName = TOPIC_NAME,
    channelName = CHANNEL_NAME,
    channelId = ChatInfo.UNDEFINED_ID,
)
val chatInfo = ChatInfo(
    topicName = ChatInfo.EMPTY_STRING,
    channelName = CHANNEL_NAME,
    channelId = 123456,
)
