package com.mironov.coursework.data.datasource.message

import com.mironov.coursework.data.database.MessageDao
import com.mironov.coursework.data.database.model.message.MessageDbModel
import javax.inject.Inject

class MessageLocalDataSourceImpl @Inject constructor(
    private val dao: MessageDao
) : MessageLocalDataSource {

    companion object {

        private const val MESSAGE_CACHE_LIMIT = 50
    }

    override suspend fun getMessages(channelName: String, topicName: String): List<MessageDbModel> =
        dao.getMessages(channelName, topicName)

    override suspend fun insertMessages(
        messages: List<MessageDbModel>,
        channelName: String,
        topicName: String,
    ) {
        dao.clearTopic(channelName, topicName)
        dao.insertMessages(messages)
    }

    override suspend fun insertOldMessages(
        messages: List<MessageDbModel>,
        channelName: String,
        topicName: String,
    ) {
        dao.insertMessages(messages)
        val tableSize = dao.getTableSize()
        if (tableSize <= MESSAGE_CACHE_LIMIT) return

        val diff = tableSize - MESSAGE_CACHE_LIMIT
        dao.deleteMessagesFromOtherTopics(channelName, topicName, diff)

        val newSize = dao.getTableSize()
        if (newSize <= MESSAGE_CACHE_LIMIT) return

        val newDiff = newSize - MESSAGE_CACHE_LIMIT
        dao.deleteNewMessages(newDiff)
    }

    override suspend fun insertNewMessages(
        messages: List<MessageDbModel>,
        channelName: String,
        topicName: String,
    ) {
        dao.insertMessages(messages)
        val tableSize = dao.getTableSize()
        if (tableSize <= MESSAGE_CACHE_LIMIT) return

        val diff = tableSize - MESSAGE_CACHE_LIMIT
        dao.deleteMessagesFromOtherTopics(channelName, topicName, diff)

        val newSize = dao.getTableSize()
        if (newSize <= MESSAGE_CACHE_LIMIT) return

        val newDiff = newSize - MESSAGE_CACHE_LIMIT
        dao.deleteOldMessages(newDiff)
    }
}