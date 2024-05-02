package com.mironov.coursework.data.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mironov.coursework.data.database.model.StreamDbModel.Companion.TABLE_NAME

@Entity(
    tableName = TABLE_NAME
)
data class StreamDbModel(
    @PrimaryKey
    val id: Int,
    @ColumnInfo(COLUMN_NAME)
    val name: String,
    @ColumnInfo(COLUMN_IS_SUBSCRIBED)
    val isSubscribed: Boolean,
) {

    companion object {

        const val TABLE_NAME = "stream"
        private const val COLUMN_NAME = "name"
        private const val COLUMN_IS_SUBSCRIBED = "is_subscribed"
    }
}