package com.mironov.coursework.data.repository

import com.mironov.coursework.data.datasource.message.MessageLocalDataSource
import com.mironov.coursework.data.datasource.message.MessageRemoteDataSource
import com.mironov.coursework.data.mapper.MY_ID
import com.mironov.coursework.data.mapper.toDbModel
import com.mironov.coursework.data.mapper.toEntity
import com.mironov.coursework.data.utils.runCatchingNonCancellation
import com.mironov.coursework.di.app.annotation.IoDispatcher
import com.mironov.coursework.domain.entity.Message
import com.mironov.coursework.domain.repository.MessageRepository
import com.mironov.coursework.domain.repository.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MessageRepositoryImpl @Inject constructor(
    @IoDispatcher private val dispatcher: CoroutineDispatcher,
    private val remoteDataSource: MessageRemoteDataSource,
    private val localDataSource: MessageLocalDataSource
) : MessageRepository {

    override suspend fun getMessages(
        channelName: String,
        topicName: String
    ): Result<List<Message>> = withContext(dispatcher) {
        runCatchingNonCancellation {
            val messagesDbModel = remoteDataSource
                .getMessages(channelName, topicName)
                .map { it.toDbModel(channelName, topicName, it.id) }
            localDataSource.insertMessages(messagesDbModel, channelName, topicName)

            val messages = localDataSource.getMessages(channelName, topicName).map {
                it.toEntity(MY_ID)
            }

            Result.Success(messages)
        }
    }

    override suspend fun sendMessages(
        channelName: String,
        topicName: String,
        content: String
    ): Result<Boolean> = withContext(dispatcher) {
        runCatchingNonCancellation {
            remoteDataSource.sendMessage(channelName, topicName, content)
            Result.Success(true)
        }
    }

    override suspend fun getMessagesById(id: Long): Result<Message> = withContext(dispatcher) {
        runCatchingNonCancellation {
            val message = remoteDataSource.getMessageById(id).toEntity(MY_ID)
            Result.Success(message)
        }
    }

    override suspend fun getMessagesCache(
        channelName: String,
        topicName: String
    ): Result<List<Message>> = withContext(dispatcher) {
        runCatchingNonCancellation {
            val messages = localDataSource.getMessages(channelName, topicName).map {
                it.toEntity(MY_ID)
            }
            Result.Success(messages)
        }
    }

    override suspend fun getPrevMessages(
        channelName: String,
        topicName: String,
        anchorMessageId: String
    ): Result<List<Message>> = withContext(dispatcher) {
        runCatchingNonCancellation {
            val messagesDbModel = remoteDataSource
                .getPrevPageMessages(channelName, topicName, anchorMessageId)
                .map { it.toDbModel(channelName, topicName, it.id) }

            localDataSource.insertOldMessages(messagesDbModel, channelName, topicName)
            val messages = localDataSource.getMessages(channelName, topicName).map {
                it.toEntity(MY_ID)
            }
            Result.Success(messages)
        }
    }

    override suspend fun getNextMessages(
        channelName: String,
        topicName: String,
        anchorMessageId: String
    ): Result<List<Message>> = withContext(dispatcher) {
        runCatchingNonCancellation {
            val messagesDbModel = remoteDataSource
                .getNextPageMessages(channelName, topicName, anchorMessageId)
                .map { it.toDbModel(channelName, topicName, it.id) }

            localDataSource.insertNewMessages(messagesDbModel, channelName, topicName)
            val messages = localDataSource.getMessages(channelName, topicName).map {
                it.toEntity(MY_ID)
            }
            Result.Success(messages)
        }
    }
}