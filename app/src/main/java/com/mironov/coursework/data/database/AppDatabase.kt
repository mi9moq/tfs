package com.mironov.coursework.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mironov.coursework.data.database.model.message.MessageInfoDbModel
import com.mironov.coursework.data.database.model.message.ReactionDbModel
import com.mironov.coursework.data.database.model.stream.StreamDbModel
import com.mironov.coursework.data.database.model.stream.TopicDbModel

@Database(
    entities = [
        StreamDbModel::class,
        TopicDbModel::class,
        MessageInfoDbModel::class,
        ReactionDbModel::class,
    ],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun messageDao(): MessageDao

    abstract fun streamDao(): StreamDao
}