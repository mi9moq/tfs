package com.mironov.coursework.data.repository

import com.mironov.coursework.data.mapper.toListChannel
import com.mironov.coursework.data.mapper.toListTopic
import com.mironov.coursework.data.network.api.ZulipApi
import com.mironov.coursework.data.utils.runCatchingNonCancellation
import com.mironov.coursework.di.annotation.IoDispatcher
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
) : ChannelRepository {

    override suspend fun gelSubscribeChannels(): Result<List<Channel>> = withContext(dispatcher) {
        runCatchingNonCancellation {
            val channels = api.getSubscribedStreams().streams.toListChannel()
            Result.Success(channels)
        }
    }

    override suspend fun gelAllChannels(): Result<List<Channel>> = withContext(dispatcher) {
        runCatchingNonCancellation {
            val channels = api.getAllStreams().streams.toListChannel()
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