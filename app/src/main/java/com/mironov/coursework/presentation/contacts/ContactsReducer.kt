package com.mironov.coursework.presentation.contacts

import com.mironov.coursework.navigation.router.ContactsRouter
import vivid.money.elmslie.core.store.dsl.ScreenDslReducer
import javax.inject.Inject

class ContactsReducer @Inject constructor(
    private val router: ContactsRouter
) : ScreenDslReducer<ContactsEvent,
        ContactsEvent.Ui,
        ContactsEvent.Domain,
        ContactsState,
        ContactsEffect,
        ContactsCommand>(
    ContactsEvent.Ui::class,
    ContactsEvent.Domain::class
) {

    override fun Result.internal(event: ContactsEvent.Domain): Any = when (event) {
        ContactsEvent.Domain.LoadingFailure -> {
            state {
                copy(isLoading = false)
            }
            effects {
                +ContactsEffect.Error
            }
        }

        is ContactsEvent.Domain.LoadingSuccess -> {
            state {
                copy(isLoading = false, users = event.users)
            }
        }

        is ContactsEvent.Domain.FilterSuccess -> {
            state {
                copy(isLoading = false, users = event.users)
            }
        }
    }

    override fun Result.ui(event: ContactsEvent.Ui): Any = when (event) {
        ContactsEvent.Ui.Initial -> {
            state {
                copy(isLoading = true)
            }
            commands {
                +ContactsCommand.Load
            }
        }

        ContactsEvent.Ui.Refresh -> {
            state {
                copy(isLoading = true)
            }
            commands {
                +ContactsCommand.Load
            }
        }

        is ContactsEvent.Ui.OpenUserProfile -> router.openProfile(event.id)

        is ContactsEvent.Ui.ChangeFilter -> {
            commands {
                +ContactsCommand.ApplyFilter(event.query)
            }
        }
    }
}