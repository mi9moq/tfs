package com.mironov.coursework.di.chat

import com.mironov.coursework.ui.chat.ChatFragment
import dagger.Subcomponent

@Subcomponent(
    modules = [
        ChatModule::class
    ]
)
interface ChatComponent {

    fun inject(fragment: ChatFragment)

    @Subcomponent.Factory
    interface Factory {

        fun create(): ChatComponent
    }
}