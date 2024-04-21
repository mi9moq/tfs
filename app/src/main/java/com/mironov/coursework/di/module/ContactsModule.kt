package com.mironov.coursework.di.module

import com.mironov.coursework.di.AppScope
import com.mironov.coursework.presentation.contacts.ContactsActor
import com.mironov.coursework.presentation.contacts.ContactsEffect
import com.mironov.coursework.presentation.contacts.ContactsEvent
import com.mironov.coursework.presentation.contacts.ContactsReducer
import com.mironov.coursework.presentation.contacts.ContactsState
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
}