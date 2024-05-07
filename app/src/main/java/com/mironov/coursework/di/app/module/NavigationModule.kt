package com.mironov.coursework.di.app.module

import com.github.terrakok.cicerone.Cicerone
import com.github.terrakok.cicerone.NavigatorHolder
import com.github.terrakok.cicerone.Router
import com.mironov.coursework.di.app.annotation.AppScope
import com.mironov.coursework.navigation.LocalCiceroneHolder
import com.mironov.coursework.navigation.router.MainRouter
import com.mironov.coursework.navigation.router.MainRouterImpl
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
    fun bindMainRouter(impl: MainRouterImpl): MainRouter
}