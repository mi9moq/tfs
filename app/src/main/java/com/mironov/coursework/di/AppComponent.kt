package com.mironov.coursework.di

import com.mironov.coursework.di.module.ChatModule
import com.mironov.coursework.di.module.DispatcherModule
import com.mironov.coursework.ui.channels.ChannelsPageFragment
import com.mironov.coursework.ui.contatcs.ContactsFragment
import com.mironov.coursework.ui.main.MainActivity
import com.mironov.coursework.ui.main.NavigationFragment
import com.mironov.coursework.ui.chat.ChatFragment
import com.mironov.coursework.ui.profile.OwnProfileFragment
import com.mironov.coursework.ui.profile.UserProfileFragment
import dagger.Component

@AppScope
@Component(
    modules = [
        NavigationModule::class,
        ViewModelModule::class,
        NetworkModule::class,
        ChatModule::class,
        DispatcherModule::class,
    ]
)
interface AppComponent {

    fun inject(activity: MainActivity)

    fun inject(fragment: ContactsFragment)

    fun inject(fragment: OwnProfileFragment)

    fun inject(fragment: UserProfileFragment)

    fun inject(fragment: ChannelsPageFragment)

    fun inject(fragment: ChatFragment)

    fun inject(fragment: NavigationFragment)
}