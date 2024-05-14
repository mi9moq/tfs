package com.mironov.coursework.stub

import com.mironov.coursework.data.database.model.message.MessageDbModel
import com.mironov.coursework.data.database.model.message.MessageInfoDbModel
import com.mironov.coursework.data.network.model.message.MessageDto
import com.mironov.coursework.data.network.model.message.MessageResponse
import com.mironov.coursework.domain.entity.Message
import com.mironov.coursework.ui.utils.groupByDate
import java.time.LocalDate

const val CHANNEL_NAME = "stub channel"

const val TOPIC_NAME = "stub topic"

const val MESSAGE_CONTENT = "stub message"

const val MESSAGE_ID = 1234L

const val EMOJI_NAME = "stub emoji"

val messages = listOf(
    Message(
        id = 2L,
        content = "Message 1",
        senderName = "S1",
        senderId = 1,
        reactions = emptyMap(),
        sendTime = LocalDate.of(2024, 4, 16)
    ),
    Message(
        id = 3L,
        content = "Message 2",
        senderName = "S2",
        senderId = 2,
        reactions = emptyMap(),
        sendTime = LocalDate.of(2024, 4, 16)
    ),
)

val messageDelegateList = messages.groupByDate()

val messagesDbModel = listOf(
    MessageDbModel(
        message = MessageInfoDbModel(
            id = 2L,
            avatarUrl = null,
            senderId = 1,
            senderName = "aaa",
            timestamp = 1713230866L,
            content = "Message 1",
            channelName = CHANNEL_NAME,
            topicName = TOPIC_NAME,
        ),
        reactions = emptyList()
    ),
    MessageDbModel(
        message = MessageInfoDbModel(
            id = 3L,
            avatarUrl = null,
            senderId = 2,
            senderName = "S2",
            timestamp = 1713230866L,
            content = "Message 2",
            channelName = CHANNEL_NAME,
            topicName = TOPIC_NAME,
        ),
        reactions = emptyList()
    ),
)

val messagesDto = listOf(
    MessageDto(
        id = 2L,
        avatarUrl = null,
        senderId = 1,
        senderName = "aaa",
        timestamp = 1713230866L,
        content = "Message 1",
        reactions = emptyList()
    ),
    MessageDto(
        id = 3L,
        avatarUrl = null,
        senderId = 2,
        senderName = "S2",
        timestamp = 1713230866L,
        content = "Message 2",
        reactions = emptyList()
    ),
)

val messagesResponse = MessageResponse(messagesDto)