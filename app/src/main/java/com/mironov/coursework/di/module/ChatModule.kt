package com.mironov.coursework.di.module

import com.mironov.coursework.data.repository.MessageRepositoryImpl
import com.mironov.coursework.data.repository.ReactionRepositoryImpl
import com.mironov.coursework.di.AppScope
import com.mironov.coursework.domain.repository.MessageRepository
import com.mironov.coursework.domain.repository.ReactionRepository
import dagger.Binds
import dagger.Module

@Module
interface ChatModule {

    @AppScope
    @Binds
    fun bindMessageRepository(impl: MessageRepositoryImpl): MessageRepository

    @AppScope
    @Binds
    fun bindReactionRepository(impl: ReactionRepositoryImpl): ReactionRepository
}