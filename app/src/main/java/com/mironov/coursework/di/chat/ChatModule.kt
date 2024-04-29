package com.mironov.coursework.di.chat

import com.mironov.coursework.navigation.router.ChatRouter
import com.mironov.coursework.navigation.router.ChatRouterImpl
import com.mironov.coursework.presentation.chat.ChatActor
import com.mironov.coursework.presentation.chat.ChatCommand
import com.mironov.coursework.presentation.chat.ChatEffect
import com.mironov.coursework.presentation.chat.ChatEvent
import com.mironov.coursework.presentation.chat.ChatReducer
import com.mironov.coursework.presentation.chat.ChatState
import dagger.Binds
import dagger.Module
import dagger.Provides
import vivid.money.elmslie.core.store.ElmStore
import vivid.money.elmslie.core.store.Store

@Module
class ChatModule {

    @Provides
    fun providesChatRouter(impl: ChatRouterImpl): ChatRouter = impl

    @Provides
    fun providesChatState(): ChatState = ChatState()

    @Provides
    fun providesChatStore(
        state: ChatState,
        actor: ChatActor,
        reducer: ChatReducer
    ): ElmStore<ChatEvent, ChatState, ChatEffect, ChatCommand> = ElmStore(
        initialState = state,
        reducer = reducer,
        actor = actor
    )
}