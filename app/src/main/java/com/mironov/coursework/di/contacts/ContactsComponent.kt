package com.mironov.coursework.di.contacts

import com.mironov.coursework.ui.contatcs.ContactsFragment
import dagger.Subcomponent

@ContactsScope
@Subcomponent(
    modules = [
        ContactsModule::class
    ]
)
interface ContactsComponent {

    fun inject(fragment: ContactsFragment)

    @Subcomponent.Factory
    interface Factory {

        fun create(): ContactsComponent
    }
}