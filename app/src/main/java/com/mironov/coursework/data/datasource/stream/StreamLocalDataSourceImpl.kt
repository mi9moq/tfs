package com.mironov.coursework.data.datasource.stream

import com.mironov.coursework.data.database.StreamDao
import com.mironov.coursework.data.database.model.stream.StreamDbModel
import com.mironov.coursework.data.database.model.stream.TopicDbModel
import javax.inject.Inject

class StreamLocalDataSourceImpl @Inject constructor(
    private val dao: StreamDao
) : StreamLocalDataSource {

    override suspend fun getAllStreams(): List<StreamDbModel> =
        dao.getAllStreams()

    override suspend fun getSubscribedStreams(): List<StreamDbModel> =
        dao.getSubscribedStreams()

    override suspend fun getTopics(streamId: Int): List<TopicDbModel> =
        dao.getTopics(streamId)

    override suspend fun insertStreams(streams: List<StreamDbModel>) =
        dao.insertStreams(streams)

    override suspend fun removeStreams(isSubscribed: Boolean) =
        dao.removeStreams(isSubscribed)
}