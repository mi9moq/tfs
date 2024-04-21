package com.mironov.coursework.di.module

import com.mironov.coursework.di.AppScope
import com.mironov.coursework.navigation.router.ContactsRouter
import com.mironov.coursework.navigation.router.ContactsRouterImpl
import com.mironov.coursework.presentation.contacts.ContactsActor
import com.mironov.coursework.presentation.contacts.ContactsEffect
import com.mironov.coursework.presentation.contacts.ContactsEvent
import com.mironov.coursework.presentation.contacts.ContactsReducer
import com.mironov.coursework.presentation.contacts.ContactsState
import dagger.Binds
import dagger.Module
import dagger.Provides
import vivid.money.elmslie.core.store.ElmStore
import vivid.money.elmslie.core.store.Store

@Module
object ContactsModule {

    @AppScope
    @Provides
    fun providesContactsState(): ContactsState = ContactsState()

    @AppScope
    @Provides
    fun providesContactsStore(
        state: ContactsState,
        actor: ContactsActor,
        reducer: ContactsReducer
    ): Store<ContactsEvent, ContactsEffect, ContactsState> = ElmStore(
        initialState = state,
        reducer = reducer,
        actor = actor
    )

    @AppScope
    @Provides
    fun providesContactsRouter(impl: ContactsRouterImpl): ContactsRouter = impl
}