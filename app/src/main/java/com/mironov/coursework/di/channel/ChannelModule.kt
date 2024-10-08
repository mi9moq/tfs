package com.mironov.coursework.di.channel

import com.mironov.coursework.navigation.router.ChannelRouter
import com.mironov.coursework.navigation.router.ChannelRouterImpl
import com.mironov.coursework.presentation.channel.ChannelActor
import com.mironov.coursework.presentation.channel.ChannelCommand
import com.mironov.coursework.presentation.channel.ChannelEffect
import com.mironov.coursework.presentation.channel.ChannelEvent
import com.mironov.coursework.presentation.channel.ChannelReducer
import com.mironov.coursework.presentation.channel.ChannelState
import dagger.Module
import dagger.Provides
import vivid.money.elmslie.core.store.ElmStore

@Module
class ChannelModule {

    @ChannelScope
    @Provides
    fun providesChannelRouter(impl: ChannelRouterImpl): ChannelRouter = impl

    @ChannelScope
    @Provides
    fun providersChannelState(): ChannelState = ChannelState()

    @ChannelScope
    @Provides
    fun providesChannelStore(
        state: ChannelState,
        actor: ChannelActor,
        reducer: ChannelReducer
    ): ElmStore<ChannelEvent, ChannelState, ChannelEffect, ChannelCommand> = ElmStore(
        initialState = state,
        reducer = reducer,
        actor = actor
    )
}