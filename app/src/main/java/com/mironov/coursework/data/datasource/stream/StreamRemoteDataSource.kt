package com.mironov.coursework.data.datasource.stream

import com.mironov.coursework.data.network.model.streams.StreamDto
import com.mironov.coursework.data.network.model.topic.TopicDto

interface StreamRemoteDataSource {

    suspend fun getAllStreams(): List<StreamDto>

    suspend fun getSubscribedStreams(): List<StreamDto>

    suspend fun getTopics(streamId: Int): List<TopicDto>

    suspend fun createStream(name: String, description: String)
}