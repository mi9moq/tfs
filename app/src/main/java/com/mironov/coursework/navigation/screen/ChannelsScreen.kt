package com.mironov.coursework.navigation.screen

import com.github.terrakok.cicerone.androidx.FragmentScreen
import com.mironov.coursework.ui.channels.ChannelsFragment

fun getChannelsScreen() = FragmentScreen {
    ChannelsFragment.newInstance()
}