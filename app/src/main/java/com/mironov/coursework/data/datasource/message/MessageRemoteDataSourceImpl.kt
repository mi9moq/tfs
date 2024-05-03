package com.mironov.coursework.data.datasource.message

import com.mironov.coursework.data.network.api.ZulipApi
import com.mironov.coursework.data.network.model.message.MessageDto
import com.mironov.coursework.data.utils.Narrow
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject

class MessageRemoteDataSourceImpl @Inject constructor(
    private val api: ZulipApi
) : MessageRemoteDataSource {

    override suspend fun getMessages(channelName: String, topicName: String): List<MessageDto> {
        val narrow = mutableListOf<Narrow>().apply {
            add(Narrow(Narrow.STREAM, channelName))
            add(Narrow(Narrow.TOPIC, topicName))
        }
        return api.getMessages(narrow = Json.encodeToString(narrow)).messages
    }

    override suspend fun getPrevPageMessages(
        channelName: String,
        topicName: String,
        anchorMessageId: String
    ): List<MessageDto> {
        val narrow = mutableListOf<Narrow>().apply {
            add(Narrow(Narrow.STREAM, channelName))
            add(Narrow(Narrow.TOPIC, topicName))
        }
        return api.getMessages(
            narrow = Json.encodeToString(narrow),
            numBefore = ZulipApi.MAX_MESSAGES,
            numAfter = ZulipApi.NULL_MESSAGES,
            anchor = anchorMessageId
        ).messages
    }

    override suspend fun getNextPageMessages(
        channelName: String,
        topicName: String,
        anchorMessageId: String
    ): List<MessageDto> {
        val narrow = mutableListOf<Narrow>().apply {
            add(Narrow(Narrow.STREAM, channelName))
            add(Narrow(Narrow.TOPIC, topicName))
        }
        return api.getMessages(
            narrow = Json.encodeToString(narrow),
            numBefore = ZulipApi.NULL_MESSAGES,
            numAfter = ZulipApi.MAX_MESSAGES,
            anchor = anchorMessageId
        ).messages
    }

    override suspend fun sendMessage(channelName: String, topicName: String, content: String) =
        api.sendMessage(to = channelName, topic = topicName, content = content)

    override suspend fun getMessagesById(id: Int): MessageDto =
        api.getMessageById(id).message

    override suspend fun addReaction(messageId: Long, emojiName: String) =
        api.addReaction(messageId, emojiName)

    override suspend fun removeReaction(messageId: Long, emojiName: String) =
        api.removeReaction(messageId, emojiName)
}