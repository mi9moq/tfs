package com.mironov.coursework.navigation.screen

import com.github.terrakok.cicerone.androidx.FragmentScreen
import com.mironov.coursework.presentation.chat.ChatInfo
import com.mironov.coursework.ui.chat.ChatFragment

fun getChatScreen(chatInfo: ChatInfo) = FragmentScreen{
    ChatFragment.newInstance(chatInfo)
}