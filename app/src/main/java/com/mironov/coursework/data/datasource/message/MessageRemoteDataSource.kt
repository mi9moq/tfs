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
        anchorMessageId: String
    ): List<MessageDto>

    suspend fun getNextPageMessages(
        channelName: String,
        topicName: String,
        anchorMessageId: String
    ): List<MessageDto>

    suspend fun sendMessage(
        channelName: String,
        topicName: String,
        content: String
    )

    suspend fun getMessagesById(id: Int): MessageDto

    suspend fun addReaction(messageId: Long, emojiName: String)

    suspend fun removeReaction(messageId: Long, emojiName: String)
}