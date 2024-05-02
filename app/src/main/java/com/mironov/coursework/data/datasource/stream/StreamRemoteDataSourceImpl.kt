package com.mironov.coursework.data.datasource.stream

import com.mironov.coursework.data.network.api.ZulipApi
import com.mironov.coursework.data.network.model.streams.StreamDto
import com.mironov.coursework.data.network.model.topic.TopicDto
import javax.inject.Inject

class StreamRemoteDataSourceImpl @Inject constructor(
    private val api: ZulipApi
) : StreamRemoteDataSource {

    override suspend fun getAllStreams(): List<StreamDto> =
        api.getAllStreams().streams

    override suspend fun getSubscribedStreams(): List<StreamDto> =
        api.getSubscribedStreams().streams

    override suspend fun getTopics(streamId: Int): List<TopicDto> =
        api.getTopics(streamId).topics
}