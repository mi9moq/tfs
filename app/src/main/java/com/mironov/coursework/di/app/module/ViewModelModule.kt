package com.mironov.coursework.di.app.module

import androidx.lifecycle.ViewModel
import com.mironov.coursework.di.app.annotation.ViewModelKey
import com.mironov.coursework.presentation.main.NavigationViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(NavigationViewModel::class)
    fun bindNavigationViewModel(impl: NavigationViewModel): ViewModel
}