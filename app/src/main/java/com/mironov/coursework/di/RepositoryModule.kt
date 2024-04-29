package com.mironov.coursework.di

import com.mironov.coursework.data.repository.ChannelRepositoryImpl
import com.mironov.coursework.data.repository.MessageRepositoryImpl
import com.mironov.coursework.data.repository.ReactionRepositoryImpl
import com.mironov.coursework.data.repository.UserRepositoryImpl
import com.mironov.coursework.domain.repository.ChannelRepository
import com.mironov.coursework.domain.repository.MessageRepository
import com.mironov.coursework.domain.repository.ReactionRepository
import com.mironov.coursework.domain.repository.UserRepository
import dagger.Binds
import dagger.Module

@Module
interface RepositoryModule {

    @AppScope
    @Binds
    fun bindChannelRepository(impl: ChannelRepositoryImpl): ChannelRepository

    @AppScope
    @Binds
    fun bindMessageRepository(impl: MessageRepositoryImpl): MessageRepository

    @AppScope
    @Binds
    fun bindReactionRepository(impl: ReactionRepositoryImpl): ReactionRepository

    @AppScope
    @Binds
    fun bindUserRepository(impl: UserRepositoryImpl): UserRepository
}