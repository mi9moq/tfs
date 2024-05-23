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
        if (topicName.isNotEmpty())
            dao.getMessages(channelName, topicName)
        else
            dao.getMessages(channelName)

    override suspend fun insertMessages(
        messages: List<MessageDbModel>,
        channelName: String,
        topicName: String,
    ) {
        if (topicName.isEmpty()) {
            dao.clearChannel(channelName)
        } else {
            dao.clearTopic(channelName, topicName)
        }
        dao.insertMessages(messages)
    }

    override suspend fun insertOldMessages(
        messages: List<MessageDbModel>,
        channelName: String,
        topicName: String,
    ) {
        if (topicName.isEmpty())
            insertOldMessagesInChannel(messages, channelName)
        else
            insertOldMessagesInTopic(messages, channelName, topicName)

    }

    override suspend fun insertNewMessages(
        messages: List<MessageDbModel>,
        channelName: String,
        topicName: String,
    ) {
        if (topicName.isEmpty())
            insertNewMessagesInChannel(messages, channelName)
        else
            insertNewMessagesInTopic(messages, channelName, topicName)
    }

    private suspend fun insertOldMessagesInTopic(
        messages: List<MessageDbModel>,
        channelName: String,
        topicName: String,
    ) {
        dao.insertMessages(messages)
        val tableSize = dao.getTableSize()
        if (tableSize <= MESSAGE_CACHE_LIMIT) return

        val diff = tableSize - MESSAGE_CACHE_LIMIT
        dao.deleteMessagesFromOtherTopic(channelName, topicName, diff)

        val newSize = dao.getTableSize()
        if (newSize <= MESSAGE_CACHE_LIMIT) return

        val newDiff = newSize - MESSAGE_CACHE_LIMIT
        dao.deleteNewMessages(newDiff)
    }

    private suspend fun insertOldMessagesInChannel(
        messages: List<MessageDbModel>,
        channelName: String,
    ) {
        dao.insertMessages(messages)
        val tableSize = dao.getTableSize()
        if (tableSize <= MESSAGE_CACHE_LIMIT) return

        val diff = tableSize - MESSAGE_CACHE_LIMIT
        dao.deleteMessagesFromOtherChannel(channelName, diff)

        val newSize = dao.getTableSize()
        if (newSize <= MESSAGE_CACHE_LIMIT) return

        val newDiff = newSize - MESSAGE_CACHE_LIMIT
        dao.deleteNewMessages(newDiff)
    }

    private suspend fun insertNewMessagesInTopic(
        messages: List<MessageDbModel>,
        channelName: String,
        topicName: String,
    ) {
        dao.insertMessages(messages)
        val tableSize = dao.getTableSize()
        if (tableSize <= MESSAGE_CACHE_LIMIT) return

        val diff = tableSize - MESSAGE_CACHE_LIMIT
        dao.deleteMessagesFromOtherTopic(channelName, topicName, diff)

        val newSize = dao.getTableSize()
        if (newSize <= MESSAGE_CACHE_LIMIT) return

        val newDiff = newSize - MESSAGE_CACHE_LIMIT
        dao.deleteOldMessages(newDiff)
    }

    private suspend fun insertNewMessagesInChannel(
        messages: List<MessageDbModel>,
        channelName: String,
    ) {
        dao.insertMessages(messages)
        val tableSize = dao.getTableSize()
        if (tableSize <= MESSAGE_CACHE_LIMIT) return

        val diff = tableSize - MESSAGE_CACHE_LIMIT
        dao.deleteMessagesFromOtherChannel(channelName, diff)

        val newSize = dao.getTableSize()
        if (newSize <= MESSAGE_CACHE_LIMIT) return

        val newDiff = newSize - MESSAGE_CACHE_LIMIT
        dao.deleteOldMessages(newDiff)
    }
}