package com.mironov.coursework.di

import androidx.lifecycle.ViewModel
import com.mironov.coursework.presentation.channel.ChannelViewModel
import com.mironov.coursework.presentation.chat.ChatViewModel
import com.mironov.coursework.presentation.contacts.ContactsViewModel
import com.mironov.coursework.presentation.main.NavigationViewModel
import com.mironov.coursework.presentation.profile.ProfileViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(ContactsViewModel::class)
    fun bindContactsViewModel(impl: ContactsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ProfileViewModel::class)
    fun bindProfileViewModel(impl: ProfileViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ChannelViewModel::class)
    fun bindChannelViewModel(impl: ChannelViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ChatViewModel::class)
    fun bindChatViewModel(impl: ChatViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(NavigationViewModel::class)
    fun bindNavigationViewModel(impl: NavigationViewModel): ViewModel
}