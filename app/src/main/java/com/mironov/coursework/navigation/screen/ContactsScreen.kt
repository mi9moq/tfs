package com.mironov.coursework.navigation.screen

import com.github.terrakok.cicerone.androidx.FragmentScreen
import com.mironov.coursework.ui.contatcs.ContactsFragment

fun getContactsScreen() = FragmentScreen {
    ContactsFragment.newInstance()
}