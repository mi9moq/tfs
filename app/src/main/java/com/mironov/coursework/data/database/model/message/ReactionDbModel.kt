package com.mironov.coursework.data.database.model.message

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.mironov.coursework.data.database.model.message.ReactionDbModel.Companion.TABLE_NAME

@Entity(
    tableName = TABLE_NAME,
    foreignKeys = [ForeignKey(
        entity = MessageInfoDbModel::class,
        parentColumns = [MessageInfoDbModel.COLUMN_ID],
        childColumns = [ReactionDbModel.COLUMN_MESSAGE_ID],
        onDelete = ForeignKey.CASCADE,
        onUpdate = ForeignKey.CASCADE,
    )]
)
data class ReactionDbModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(COLUMN_EMOJI_CODE)
    val emojiCode: String,
    @ColumnInfo(COLUMN_EMOJI_NAME)
    val emojiName: String,
    @ColumnInfo(COLUMN_USER_ID)
    val userId: Int,
    @ColumnInfo(COLUMN_MESSAGE_ID)
    val messageId: Long
) {

    companion object {

        const val TABLE_NAME = "reaction"
        private const val COLUMN_EMOJI_CODE = "emoji_code"
        private const val COLUMN_EMOJI_NAME = "emoji_name"
        private const val COLUMN_USER_ID = "user_id"
        const val COLUMN_MESSAGE_ID = "message_id"
    }
}