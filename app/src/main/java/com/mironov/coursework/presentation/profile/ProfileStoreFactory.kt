package com.mironov.coursework.presentation.profile

import vivid.money.elmslie.core.store.ElmStore
import vivid.money.elmslie.core.store.Store
import javax.inject.Inject

class ProfileStoreFactory @Inject constructor(
    private val actor: ProfileActor,
    private val reducer: ProfileReducer,
) {

    fun create(): Store<ProfileEvent, ProfileEffect, ProfileState> = ElmStore(
        initialState = ProfileState(),
        reducer = reducer,
        actor = actor
    )
}