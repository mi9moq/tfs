package com.mironov.coursework.data.database.model.message

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mironov.coursework.data.database.model.message.MessageInfoDbModel.Companion.TABLE_NAME

@Entity(tableName = TABLE_NAME)
data class MessageInfoDbModel(
    @PrimaryKey
    @ColumnInfo(COLUMN_ID)
    val id: Long,
    @ColumnInfo(COLUMN_AVATAR_URL)
    val avatarUrl: String?,
    @ColumnInfo(COLUMN_SENDER_ID)
    val senderId: Int,
    @ColumnInfo(COLUMN_SENDER_NAME)
    val senderName: String,
    @ColumnInfo(COLUMN_TIMESTAMP)
    val timestamp: Long,
    @ColumnInfo(COLUMN_CONTENT)
    val content: String,
    @ColumnInfo(COLUMN_CHANNEL_NAME)
    val channelName: String,
    @ColumnInfo(COLUMN_TOPIC_NAME)
    val topicName: String
) {

    companion object {

        const val TABLE_NAME = "message"
        const val COLUMN_ID = "id"
        private const val COLUMN_AVATAR_URL = "avatar_url"
        private const val COLUMN_SENDER_ID = "sender_id"
        private const val COLUMN_SENDER_NAME = "sender_name"
        private const val COLUMN_TIMESTAMP = "timestamp"
        private const val COLUMN_CONTENT = "content"
        private const val COLUMN_CHANNEL_NAME = "channel_name"
        private const val COLUMN_TOPIC_NAME = "topic_name"
    }
}