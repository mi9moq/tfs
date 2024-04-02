package com.mironov.coursework.navigation.screen

import com.github.terrakok.cicerone.androidx.FragmentScreen
import com.mironov.coursework.ui.message.ChatFragment

fun getChatScreen(chatId: Int) = FragmentScreen{
    ChatFragment.newInstance(chatId)
}