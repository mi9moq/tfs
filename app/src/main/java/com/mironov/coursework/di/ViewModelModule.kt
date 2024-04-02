package com.mironov.coursework.di

import androidx.lifecycle.ViewModel
import com.mironov.coursework.presentation.contacts.ContactsViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(ContactsViewModel::class)
    fun bindContactsViewModel(impl: ContactsViewModel): ViewModel
}