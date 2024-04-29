package com.mironov.coursework.di.channel

import com.mironov.coursework.ui.channels.ChannelsPageFragment
import dagger.Subcomponent

@Subcomponent(
    modules = [
        ChannelModule::class
    ]
)
interface ChannelComponent {

    fun inject(fragment: ChannelsPageFragment)

    @Subcomponent.Factory
    interface Factory {

        fun create(): ChannelComponent
    }
}