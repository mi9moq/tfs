package com.mironov.coursework.data.database

import androidx.room.Dao
import androidx.room.Query
import com.mironov.coursework.data.database.model.message.MessageDbModel
import com.mironov.coursework.data.database.model.message.MessageInfoDbModel

@Dao
interface MessageDao {

    @Query(
        "SELECT * FROM ${MessageInfoDbModel.TABLE_NAME} " +
                "WHERE channel_name = :channelName AND topic_name = :topicName"
    )
    suspend fun getMessages(channelName: String, topicName: String): List<MessageDbModel>
}