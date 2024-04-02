package com.mironov.coursework.di

import com.github.terrakok.cicerone.Cicerone
import com.github.terrakok.cicerone.NavigatorHolder
import com.github.terrakok.cicerone.Router
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
    }
}