package com.mironov.coursework.data.repository

import com.mironov.coursework.data.datasource.stream.StreamLocalDataSource
import com.mironov.coursework.data.datasource.stream.StreamRemoteDataSource
import com.mironov.coursework.data.mapper.toDbModel
import com.mironov.coursework.data.mapper.toListChannel
import com.mironov.coursework.data.mapper.toListTopic
import com.mironov.coursework.data.network.api.ZulipApi
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
    private val api: ZulipApi,
    @IoDispatcher private val dispatcher: CoroutineDispatcher,
    private val localDataSource: StreamLocalDataSource,
    private val remoteDataSource: StreamRemoteDataSource
) : ChannelRepository {

    override suspend fun gelSubscribeChannels(): Result<List<Channel>> = withContext(dispatcher) {
        runCatchingNonCancellation {
            val isSubscribed = true
            localDataSource.removeStreams(isSubscribed)
            val streamsDbModel = remoteDataSource.getSubscribedStreams().map {
                it.toDbModel(isSubscribed)
            }
            localDataSource.insertStreams(streamsDbModel)
            val channels = localDataSource.getSubscribedStreams().toListChannel()
            Result.Success(channels)
        }
    }

    override suspend fun gelAllChannels(): Result<List<Channel>> = withContext(dispatcher) {
        runCatchingNonCancellation {
            val isSubscribed = false
            localDataSource.removeStreams(isSubscribed)
            val streamsDbModel = remoteDataSource.getAllStreams().map {
                it.toDbModel(isSubscribed)
            }
            localDataSource.insertStreams(streamsDbModel)
            val channels = localDataSource.getAllStreams().toListChannel()
            Result.Success(channels)
        }
    }

    override suspend fun getTopics(channel: Channel): Result<List<Topic>> =
        withContext(dispatcher) {
            runCatchingNonCancellation {
                val topics = api.getTopics(channel.id).toListTopic(channel.name)
                Result.Success(topics)
            }
        }
}