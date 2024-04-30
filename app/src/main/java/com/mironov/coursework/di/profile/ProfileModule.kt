package com.mironov.coursework.di.profile

import com.mironov.coursework.navigation.router.ProfileRouter
import com.mironov.coursework.navigation.router.ProfileRouterImpl
import com.mironov.coursework.presentation.profile.ProfileActor
import com.mironov.coursework.presentation.profile.ProfileCommand
import com.mironov.coursework.presentation.profile.ProfileEffect
import com.mironov.coursework.presentation.profile.ProfileEvent
import com.mironov.coursework.presentation.profile.ProfileReducer
import com.mironov.coursework.presentation.profile.ProfileState
import dagger.Module
import dagger.Provides
import vivid.money.elmslie.core.store.ElmStore

@Module
class ProfileModule {

    @ProfileScope
    @Provides
    fun profileState(): ProfileState = ProfileState()

    @ProfileScope
    @Provides
    fun providesProfileStore(
        state: ProfileState,
        actor: ProfileActor,
        reducer: ProfileReducer
    ): ElmStore<ProfileEvent, ProfileState, ProfileEffect, ProfileCommand> = ElmStore(
        initialState = state,
        reducer = reducer,
        actor = actor
    )

    @ProfileScope
    @Provides
    fun providesProfileRouter(impl: ProfileRouterImpl): ProfileRouter = impl
}