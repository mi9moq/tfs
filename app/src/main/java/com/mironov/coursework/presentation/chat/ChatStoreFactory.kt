package com.mironov.coursework.presentation.chat

import vivid.money.elmslie.core.store.ElmStore
import vivid.money.elmslie.core.store.Store
import javax.inject.Inject

class ChatStoreFactory @Inject constructor(
    private val actor: ChatActor,
    private val reducer: ChatReducer
) {

    fun create(): Store<ChatEvent, ChatEffect, ChatState> = ElmStore(
        initialState = ChatState(),
        reducer = reducer,
        actor = actor
    )
}