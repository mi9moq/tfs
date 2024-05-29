package com.mironov.coursework.data.database.model.stream

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mironov.coursework.data.database.model.stream.TopicDbModel.Companion.TABLE_NAME

@Entity(tableName = TABLE_NAME)
data class TopicDbModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int = DEFAULT_ID,
    @ColumnInfo(COLUMN_NAME)
    val name: String,
    @ColumnInfo(COLUMN_STREAM_ID)
    val streamId: Int,
) {

    companion object {

        const val TABLE_NAME = "topic"
        private const val DEFAULT_ID = 0
        private const val COLUMN_NAME = "name"
        private const val COLUMN_STREAM_ID = "stream_id"
    }
}