package com.mironov.coursework.di.app

import android.content.Context
import com.mironov.coursework.ZulipApp
import com.mironov.coursework.di.app.annotation.AppScope
import com.mironov.coursework.di.app.module.BaseUrlModule
import com.mironov.coursework.di.app.module.DataSourceModule
import com.mironov.coursework.di.app.module.DatabaseModule
import com.mironov.coursework.di.app.module.DispatcherModule
import com.mironov.coursework.di.app.module.NavigationModule
import com.mironov.coursework.di.app.module.NetworkModule
import com.mironov.coursework.di.app.module.RepositoryModule
import com.mironov.coursework.di.app.module.ViewModelModule
import com.mironov.coursework.di.channel.ChannelComponent
import com.mironov.coursework.di.chat.ChatComponent
import com.mironov.coursework.di.contacts.ContactsComponent
import com.mironov.coursework.di.profile.ProfileComponent
import com.mironov.coursework.ui.main.MainActivity
import com.mironov.coursework.ui.main.NavigationFragment
import dagger.BindsInstance
import dagger.Component

@AppScope
@Component(
    modules = [
        NavigationModule::class,
        ViewModelModule::class,
        NetworkModule::class,
        DispatcherModule::class,
        RepositoryModule::class,
        DatabaseModule::class,
        DataSourceModule::class,
        BaseUrlModule::class,
    ]
)
interface AppComponent {

    fun inject(application: ZulipApp)

    fun inject(activity: MainActivity)

    fun inject(fragment: NavigationFragment)

    fun getChatComponentFactory(): ChatComponent.Factory

    fun getChannelComponentFactory(): ChannelComponent.Factory

    fun geContactsComponentFactory(): ContactsComponent.Factory

    fun geProfileComponentFactory(): ProfileComponent.Factory

    @Component.Factory
    interface Factory {

        fun create(
            @BindsInstance context: Context
        ): AppComponent
    }
}