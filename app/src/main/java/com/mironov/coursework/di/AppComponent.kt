package com.mironov.coursework.di

import com.mironov.coursework.di.channel.ChannelComponent
import com.mironov.coursework.di.channel.ChannelModule
import com.mironov.coursework.di.chat.ChatComponent
import com.mironov.coursework.di.module.ContactsModule
import com.mironov.coursework.di.module.DispatcherModule
import com.mironov.coursework.di.module.UserModule
import com.mironov.coursework.ui.contatcs.ContactsFragment
import com.mironov.coursework.ui.main.MainActivity
import com.mironov.coursework.ui.main.NavigationFragment
import com.mironov.coursework.ui.profile.OwnProfileFragment
import com.mironov.coursework.ui.profile.UserProfileFragment
import dagger.Component

@AppScope
@Component(
    modules = [
        NavigationModule::class,
        ViewModelModule::class,
        NetworkModule::class,
        DispatcherModule::class,
        UserModule::class,
        ContactsModule::class,
        RepositoryModule::class,
    ]
)
interface AppComponent {

    fun inject(activity: MainActivity)

    fun inject(fragment: ContactsFragment)

    fun inject(fragment: OwnProfileFragment)

    fun inject(fragment: UserProfileFragment)

    fun inject(fragment: NavigationFragment)

    fun getChatComponentFactory(): ChatComponent.Factory

    fun getChannelComponentFactory(): ChannelComponent.Factory
}