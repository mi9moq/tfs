package com.mironov.coursework.di.app.module

import android.content.Context
import androidx.room.Room
import com.mironov.coursework.data.database.AppDatabase
import com.mironov.coursework.data.database.MessageDao
import com.mironov.coursework.data.database.StreamDao
import com.mironov.coursework.di.app.annotation.AppScope
import dagger.Module
import dagger.Provides

@Module
object DatabaseModule {

    private const val DB_NAME = "app_database.db"

    @AppScope
    @Provides
    fun provideAppDatabase(context: Context): AppDatabase = Room.databaseBuilder(
        context,
        AppDatabase::class.java,
        DB_NAME
    ).build()

    @AppScope
    @Provides
    fun providesMessageDao(database: AppDatabase): MessageDao = database.messageDao()

    @AppScope
    @Provides
    fun providesStreamDao(database: AppDatabase): StreamDao = database.streamDao()
}