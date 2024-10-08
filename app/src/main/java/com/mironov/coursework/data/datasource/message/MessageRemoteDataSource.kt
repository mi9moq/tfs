package com.mironov.coursework.data.datasource.message

import com.mironov.coursework.data.network.model.message.MessageDto

interface MessageRemoteDataSource {

    suspend fun getMessages(
        channelName: String,
        topicName: String
    ): List<MessageDto>

    suspend fun getPrevPageMessages(
        channelName: String,
        topicName: String,
        anchorMessageId: Long
    ): List<MessageDto>

    suspend fun getNextPageMessages(
        channelName: String,
        topicName: String,
        anchorMessageId: Long
    ): List<MessageDto>

    suspend fun sendMessage(
        channelName: String,
        topicName: String,
        content: String
    )

    suspend fun getMessageById(id: Long): MessageDto

    suspend fun editMessageContent(messageId: Long, content: String)

    suspend fun editMessageTopic(messageId: Long, topic: String)

    suspend fun deleteMessage(messageId: Long)
}