package com.mironov.coursework.di

import com.mironov.coursework.ui.main.MainActivity
import dagger.Component

@AppScope
@Component(
    modules = [
        NavigationModule::class,
        ViewModelModule::class,
    ]
)
interface AppComponent {

    fun inject(activity: MainActivity)
}