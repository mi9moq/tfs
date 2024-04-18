package com.mironov.coursework.domain.repository

import com.mironov.coursework.domain.entity.Message

interface MessageRepository {

    suspend fun getMessages(channelName: String, topicName: String): Result<List<Message>>

    suspend fun sendMessages(channelName: String, topicName: String, content: String): Result<Boolean>
}