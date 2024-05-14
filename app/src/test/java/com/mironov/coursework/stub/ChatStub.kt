package com.mironov.coursework.stub

import com.mironov.coursework.domain.entity.Message
import com.mironov.coursework.ui.utils.groupByDate
import java.time.LocalDate

const val CHANNEL_NAME = "stub channel"

const val TOPIC_NAME = "stub topic"

const val MESSAGE_CONTENT = "stub message"

const val MESSAGE_ID = 1234L

const val EMOJI_NAME = "stub emoji"

private val messages = listOf(
    Message(
        id = 2L,
        content = "c",
        senderName = "aaa",
        senderId = 1,
        reactions = emptyMap(),
        sendTime = LocalDate.of(2000, 6, 10)
    ),
    Message(
        id = 3L,
        content = "c",
        senderName = "aaa",
        senderId = 2,
        reactions = emptyMap(),
        sendTime = LocalDate.of(2001, 6, 10)
    ),
)
val messageDelegateList = messages.groupByDate()