package com.mironov.coursework.di.module

import com.mironov.coursework.data.repository.ChannelRepositoryImpl
import com.mironov.coursework.di.AppScope
import com.mironov.coursework.domain.repository.ChannelRepository
import com.mironov.coursework.navigation.router.ChannelRouter
import com.mironov.coursework.navigation.router.ChannelRouterImpl
import dagger.Binds
import dagger.Module

@Module
interface ChannelModule {

    @AppScope
    @Binds
    fun bindChannelRepository(impl: ChannelRepositoryImpl): ChannelRepository

    @AppScope
    @Binds
    fun bindChannelRouter(impl: ChannelRouterImpl): ChannelRouter
}