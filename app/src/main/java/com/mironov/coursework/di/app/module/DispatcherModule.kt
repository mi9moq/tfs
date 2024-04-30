package com.mironov.coursework.di.app.module

import com.mironov.coursework.di.app.annotation.AppScope
import com.mironov.coursework.di.app.annotation.DefaultDispatcher
import com.mironov.coursework.di.app.annotation.IoDispatcher
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

@Module
object DispatcherModule {

    @AppScope
    @Provides
    @IoDispatcher
    fun provideIoDispatcher(): CoroutineDispatcher = Dispatchers.IO

    @AppScope
    @Provides
    @DefaultDispatcher
    fun provideDefaultDispatcher(): CoroutineDispatcher = Dispatchers.Default
}