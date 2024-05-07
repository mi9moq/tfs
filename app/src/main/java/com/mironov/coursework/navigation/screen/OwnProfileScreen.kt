package com.mironov.coursework.navigation.screen

import com.github.terrakok.cicerone.androidx.FragmentScreen
import com.mironov.coursework.ui.profile.OwnProfileFragment

fun getOwnProfileScreen() = FragmentScreen {
    OwnProfileFragment.newInstance()
}