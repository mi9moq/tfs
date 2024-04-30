package com.mironov.coursework.di.contacts

import com.mironov.coursework.navigation.router.ContactsRouter
import com.mironov.coursework.navigation.router.ContactsRouterImpl
import com.mironov.coursework.presentation.contacts.ContactsActor
import com.mironov.coursework.presentation.contacts.ContactsCommand
import com.mironov.coursework.presentation.contacts.ContactsEffect
import com.mironov.coursework.presentation.contacts.ContactsEvent
import com.mironov.coursework.presentation.contacts.ContactsReducer
import com.mironov.coursework.presentation.contacts.ContactsState
import dagger.Module
import dagger.Provides
import vivid.money.elmslie.core.store.ElmStore

@Module
class ContactsModule {

    @Provides
    fun providesContactsState(): ContactsState = ContactsState()

    @Provides
    fun providesContactsStore(
        state: ContactsState,
        actor: ContactsActor,
        reducer: ContactsReducer
    ): ElmStore<ContactsEvent, ContactsState, ContactsEffect, ContactsCommand> = ElmStore(
        initialState = state,
        reducer = reducer,
        actor = actor
    )

    @Provides
    fun providesContactsRouter(impl: ContactsRouterImpl): ContactsRouter = impl
}