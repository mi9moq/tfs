package com.mironov.coursework.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.mironov.coursework.data.database.model.message.MessageDbModel
import com.mironov.coursework.data.database.model.message.MessageInfoDbModel
import com.mironov.coursework.data.database.model.message.ReactionDbModel

@Dao
interface MessageDao {

    @Query(
        "SELECT * FROM ${MessageInfoDbModel.TABLE_NAME} " +
                "WHERE channel_name = :channelName AND topic_name = :topicName"
    )
    suspend fun getMessages(channelName: String, topicName: String): List<MessageDbModel>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(messageDbModel: MessageInfoDbModel)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReaction(reactionDbModel: ReactionDbModel)

    @Transaction
    suspend fun insertMessages(messages: List<MessageDbModel>) {
        messages.forEach { messageFullInfo ->
            insertMessage(messageFullInfo.message)
            messageFullInfo.reactions.forEach {
                insertReaction(it)
            }
        }
    }

    @Query(
        "DELETE FROM ${MessageInfoDbModel.TABLE_NAME} " +
                "WHERE (channel_name != :channelName AND topic_name != :topicName)AND rowid IN (SELECT rowid FROM ${MessageInfoDbModel.TABLE_NAME} LIMIT :messageCount)"
    )
    suspend fun deleteMessagesFromOtherTopics(
        channelName: String,
        topicName: String,
        messageCount: Int
    )

    @Query(
        "DELETE FROM ${MessageInfoDbModel.TABLE_NAME} " +
                "WHERE id IN (SELECT id FROM Message ORDER BY id DESC LIMIT :messageCount)"
    )
    suspend fun deleteNewMessages(messageCount: Int)

    @Query(
        "DELETE FROM ${MessageInfoDbModel.TABLE_NAME} " +
                "WHERE id IN (SELECT id FROM Message ORDER BY id ASC LIMIT :messageCount)"
    )
    suspend fun deleteOldMessages(messageCount: Int)

    @Query("SELECT COUNT(*) FROM ${MessageInfoDbModel.TABLE_NAME}")
    suspend fun getTableSize(): Int

    @Query(
        "DELETE FROM ${MessageInfoDbModel.TABLE_NAME} " +
                "WHERE channel_name = :channelName AND topic_name = :topicName"
    )
    suspend fun clearTopic(channelName: String, topicName: String)
}