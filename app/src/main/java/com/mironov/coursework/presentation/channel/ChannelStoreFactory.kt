package com.mironov.coursework.presentation.channel

import vivid.money.elmslie.core.store.ElmStore
import vivid.money.elmslie.core.store.Store
import javax.inject.Inject

class ChannelStoreFactory @Inject constructor(
    private val actor: ChannelActor,
    private val reducer: ChannelReducer
) {

    fun create(): Store<ChannelEvent, ChannelEffect, ChannelState> = ElmStore(
        initialState = ChannelState(),
        reducer = reducer,
        actor = actor
    )
}