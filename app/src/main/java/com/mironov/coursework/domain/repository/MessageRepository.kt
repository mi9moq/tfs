package com.mironov.coursework.domain.repository

import com.mironov.coursework.domain.entity.Message

interface MessageRepository {

    suspend fun getMessages(channelName: String, topicName: String): Result<List<Message>>

    suspend fun sendMessages(
        channelName: String,
        topicName: String,
        content: String
    ): Result<Boolean>

    suspend fun getMessagesById(id: Long): Result<Message>

    suspend fun getMessagesCache(channelName: String, topicName: String): Result<List<Message>>

    suspend fun getPrevMessages(
        channelName: String,
        topicName: String,
        anchorMessageId: Long
    ): Result<List<Message>>

    suspend fun getNextMessages(
        channelName: String,
        topicName: String,
        anchorMessageId: Long
    ): Result<List<Message>>

    suspend fun editMessageContent(messageId: Long, content: String): Result<Boolean>

    suspend fun editMessageTopic(messageId: Long, topic: String): Result<Boolean>

    suspend fun deleteMessage(messageId: Long): Result<Boolean>
}