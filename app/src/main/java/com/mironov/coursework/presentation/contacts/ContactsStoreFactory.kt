package com.mironov.coursework.presentation.contacts

import vivid.money.elmslie.core.store.ElmStore
import vivid.money.elmslie.core.store.Store
import javax.inject.Inject

class ContactsStoreFactory @Inject constructor(
    private val actor: ContactsActor,
    private val reducer: ContactsReducer,
) {

    fun create(): Store<ContactsEvent, ContactsEffect, ContactsState> = ElmStore(
        initialState = ContactsState(),
        reducer = reducer,
        actor = actor
    )
}