package com.mironov.coursework.di

import com.github.terrakok.cicerone.Cicerone
import com.github.terrakok.cicerone.NavigatorHolder
import com.github.terrakok.cicerone.Router
import com.mironov.coursework.navigation.LocalCiceroneHolder
import com.mironov.coursework.navigation.router.ContactsRouter
import com.mironov.coursework.navigation.router.ContactsRouterImpl
import com.mironov.coursework.navigation.router.ProfileRouter
import com.mironov.coursework.navigation.router.ProfileRouterImpl
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
interface NavigationModule {

    companion object {

        private val cicerone: Cicerone<Router> = Cicerone.create()

        @AppScope
        @Provides
        fun provideRouter(): Router = cicerone.router

        @AppScope
        @Provides
        fun provideNavigatorHolder(): NavigatorHolder = cicerone.getNavigatorHolder()

        @AppScope
        @Provides
        fun provideLocalNavigationHolder(): LocalCiceroneHolder = LocalCiceroneHolder()
    }

    @AppScope
    @Binds
    fun bindContactsRouter(impl: ContactsRouterImpl): ContactsRouter

    @AppScope
    @Binds
    fun bindProfileRouter(impl: ProfileRouterImpl): ProfileRouter
}