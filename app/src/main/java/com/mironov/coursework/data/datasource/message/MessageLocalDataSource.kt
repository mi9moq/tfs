package com.mironov.coursework.data.datasource.message

import com.mironov.coursework.data.database.model.message.MessageDbModel

interface MessageLocalDataSource {

    suspend fun getMessages(
        channelName: String,
        topicName: String
    ): List<MessageDbModel>

    suspend fun insertMessages(
        messages: List<MessageDbModel>,
        channelName: String,
        topicName: String,
        )

    suspend fun insertOldMessages(
        messages: List<MessageDbModel>,
        channelName: String,
        topicName: String
    )

    suspend fun insertNewMessages(
        messages: List<MessageDbModel>,
        channelName: String,
        topicName: String,
        )
}