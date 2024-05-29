package com.mironov.coursework.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mironov.coursework.data.database.model.stream.StreamDbModel
import com.mironov.coursework.data.database.model.stream.TopicDbModel

@Dao
interface StreamDao {

    @Query("SELECT * FROM ${StreamDbModel.TABLE_NAME}")
    suspend fun getAllStreams(): List<StreamDbModel>

    @Query("SELECT * FROM ${StreamDbModel.TABLE_NAME} WHERE is_subscribed = 1")
    suspend fun getSubscribedStreams(): List<StreamDbModel>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertStreams(streams: List<StreamDbModel>)

    @Query("DELETE FROM ${StreamDbModel.TABLE_NAME} WHERE is_subscribed = :isSubscribed")
    suspend fun removeStreams(isSubscribed: Boolean)

    @Query("DELETE FROM ${StreamDbModel.TABLE_NAME} WHERE name = :name")
    suspend fun removeStreams(name: String)

    @Query("SELECT * FROM ${TopicDbModel.TABLE_NAME} WHERE stream_id = :streamId")
    suspend fun getTopics(streamId: Int): List<TopicDbModel>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTopics(streams: List<TopicDbModel>)

    @Query("DELETE FROM ${TopicDbModel.TABLE_NAME} WHERE stream_id = :streamId")
    fun removeTopics(streamId: Int)
}