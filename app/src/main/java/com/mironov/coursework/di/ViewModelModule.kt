package com.mironov.coursework.di

import androidx.lifecycle.ViewModel
import com.mironov.coursework.presentation.channel.ChannelViewModel
import com.mironov.coursework.presentation.main.NavigationViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(ChannelViewModel::class)
    fun bindChannelViewModel(impl: ChannelViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(NavigationViewModel::class)
    fun bindNavigationViewModel(impl: NavigationViewModel): ViewModel
}