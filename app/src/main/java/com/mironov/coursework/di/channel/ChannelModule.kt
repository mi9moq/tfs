package com.mironov.coursework.di.channel

import com.mironov.coursework.data.repository.ChannelRepositoryImpl
import com.mironov.coursework.di.AppScope
import com.mironov.coursework.domain.repository.ChannelRepository
import com.mironov.coursework.navigation.router.ChannelRouter
import com.mironov.coursework.navigation.router.ChannelRouterImpl
import com.mironov.coursework.presentation.channel.ChannelActor
import com.mironov.coursework.presentation.channel.ChannelCommand
import com.mironov.coursework.presentation.channel.ChannelEffect
import com.mironov.coursework.presentation.channel.ChannelEvent
import com.mironov.coursework.presentation.channel.ChannelReducer
import com.mironov.coursework.presentation.channel.ChannelState
import dagger.Binds
import dagger.Module
import dagger.Provides
import vivid.money.elmslie.core.store.ElmStore

@Module
class ChannelModule {

    @Provides
    fun providesChannelRouter(impl: ChannelRouterImpl): ChannelRouter = impl

    @Provides
    fun providersChannelState(): ChannelState = ChannelState()

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