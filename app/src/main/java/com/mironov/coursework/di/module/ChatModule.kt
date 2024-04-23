package com.mironov.coursework.di.module

import com.mironov.coursework.data.repository.MessageRepositoryImpl
import com.mironov.coursework.data.repository.ReactionRepositoryImpl
import com.mironov.coursework.di.AppScope
import com.mironov.coursework.domain.repository.MessageRepository
import com.mironov.coursework.domain.repository.ReactionRepository
import com.mironov.coursework.navigation.router.ChatRouter
import com.mironov.coursework.navigation.router.ChatRouterImpl
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

    @AppScope
    @Binds
    fun bindChatRouter(impl: ChatRouterImpl): ChatRouter
}