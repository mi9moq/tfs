package com.mironov.coursework.data.datasource.stream

import com.mironov.coursework.data.database.model.stream.StreamDbModel
import com.mironov.coursework.data.database.model.stream.TopicDbModel

interface StreamLocalDataSource {

    suspend fun getAllStreams(): List<StreamDbModel>

    suspend fun getSubscribedStreams(): List<StreamDbModel>

    suspend fun getTopics(streamId: Int): List<TopicDbModel>

    suspend fun insertStreams(streams: List<StreamDbModel>)

    suspend fun removeStreams(isSubscribed: Boolean)
}