package com.mironov.coursework.navigation.screen

import com.github.terrakok.cicerone.androidx.FragmentScreen
import com.mironov.coursework.ui.profile.UserProfileFragment

fun getProfileScreen(userId: Int) = FragmentScreen{
    UserProfileFragment.newInstance(userId)
}