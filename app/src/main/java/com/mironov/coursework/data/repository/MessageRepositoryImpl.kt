package com.mironov.coursework.data.repository

import com.mironov.coursework.data.mapper.MY_ID
import com.mironov.coursework.data.mapper.toListEntity
import com.mironov.coursework.data.network.api.ZulipApi
import com.mironov.coursework.data.utils.runCatchingNonCancellation
import com.mironov.coursework.di.annotation.IoDispatcher
import com.mironov.coursework.domain.entity.Message
import com.mironov.coursework.domain.repository.MessageRepository
import com.mironov.coursework.domain.repository.Result
import com.mironov.coursework.data.utils.Narrow
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject

class MessageRepositoryImpl @Inject constructor(
    private val api: ZulipApi,
    @IoDispatcher private val dispatcher: CoroutineDispatcher,
) : MessageRepository {

    override suspend fun getMessages(
        channelName: String,
        topicName: String
    ): Result<List<Message>> = withContext(dispatcher) {
        runCatchingNonCancellation {
            val narrow = mutableListOf<Narrow>().apply {
                add(Narrow(Narrow.STREAM, channelName))
                add(Narrow(Narrow.TOPIC, topicName))
            }

            val messages = api.getMessages(narrow = Json.encodeToString(narrow))
                .messages.toListEntity(MY_ID)

            Result.Success(messages)
        }
    }

    override suspend fun sendMessages(
        channelName: String,
        topicName: String,
        content: String
    ): Result<Boolean> = withContext(dispatcher) {
        runCatchingNonCancellation {
            api.sendMessage(to = channelName, topic = topicName, content = content)
            Result.Success(true)
        }
    }
}