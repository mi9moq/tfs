package com.mironov.coursework.di.app.module

import com.mironov.coursework.data.datasource.stream.StreamLocalDataSource
import com.mironov.coursework.data.datasource.stream.StreamLocalDataSourceImpl
import com.mironov.coursework.data.datasource.stream.StreamRemoteDataSource
import com.mironov.coursework.data.datasource.stream.StreamRemoteDataSourceImpl
import com.mironov.coursework.di.app.annotation.AppScope
import dagger.Binds
import dagger.Module

@Module
interface DataSourceModule {

    @AppScope
    @Binds
    fun bindStreamRemoteDataSource(impl: StreamRemoteDataSourceImpl): StreamRemoteDataSource

    @AppScope
    @Binds
    fun bindStreamLocalDataSource(impl: StreamLocalDataSourceImpl): StreamLocalDataSource
}