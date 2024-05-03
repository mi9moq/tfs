package com.mironov.coursework.data.repository

import com.mironov.coursework.data.datasource.stream.StreamLocalDataSource
import com.mironov.coursework.data.datasource.stream.StreamRemoteDataSource
import com.mironov.coursework.data.mapper.toDbModel
import com.mironov.coursework.data.mapper.toListChannel
import com.mironov.coursework.data.mapper.toTopic
import com.mironov.coursework.data.utils.runCatchingNonCancellation
import com.mironov.coursework.di.app.annotation.IoDispatcher
import com.mironov.coursework.domain.entity.Channel
import com.mironov.coursework.domain.entity.Topic
import com.mironov.coursework.domain.repository.ChannelRepository
import com.mironov.coursework.domain.repository.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ChannelRepositoryImpl @Inject constructor(
    @IoDispatcher private val dispatcher: CoroutineDispatcher,
    private val localDataSource: StreamLocalDataSource,
    private val remoteDataSource: StreamRemoteDataSource
) : ChannelRepository {

    override suspend fun gelSubscribeChannels(): Result<List<Channel>> = withContext(dispatcher) {
        runCatchingNonCancellation {
            val isSubscribed = true
            val streamsDbModel = remoteDataSource.getSubscribedStreams().map {
                it.toDbModel(isSubscribed)
            }
            localDataSource.removeStreams(isSubscribed)
            localDataSource.insertStreams(streamsDbModel)
            val channels = localDataSource.getSubscribedStreams().toListChannel()
            Result.Success(channels)
        }
    }

    override suspend fun gelAllChannels(): Result<List<Channel>> = withContext(dispatcher) {
        runCatchingNonCancellation {
            val isSubscribed = false
            val streamsDbModel = remoteDataSource.getAllStreams().map {
                it.toDbModel(isSubscribed)
            }
            localDataSource.removeStreams(isSubscribed)
            localDataSource.insertStreams(streamsDbModel)
            val channels = localDataSource.getAllStreams().toListChannel()
            Result.Success(channels)
        }
    }

    override suspend fun getTopics(channel: Channel): Result<List<Topic>> =
        withContext(dispatcher) {
            runCatchingNonCancellation {
                val streamId = channel.id
                val topicsDbModel = remoteDataSource.getTopics(streamId).map {
                    it.toDbModel(streamId)
                }
                localDataSource.removeTopics(streamId)
                localDataSource.insertTopics(topicsDbModel)
                val topics = localDataSource.getTopics(streamId).map {
                    it.toTopic(channel.name)
                }
                Result.Success(topics)
            }
        }

    override suspend fun gelSubscribeChannelsCache(): Result<List<Channel>> =
        withContext(dispatcher) {
            runCatchingNonCancellation {
                val channels = localDataSource.getSubscribedStreams().toListChannel()
                Result.Success(channels)
            }
        }

    override suspend fun gelAllChannelsCache(): Result<List<Channel>> =
        withContext(dispatcher) {
            runCatchingNonCancellation {
                val channels = localDataSource.getAllStreams().toListChannel()
                Result.Success(channels)
            }
        }

    override suspend fun getTopicsCache(channel: Channel): Result<List<Topic>> =
        withContext(dispatcher) {
            runCatchingNonCancellation {
                val topics = localDataSource.getTopics(channel.id).map {
                    it.toTopic(channel.name)
                }
                Result.Success(topics)
            }
        }
}