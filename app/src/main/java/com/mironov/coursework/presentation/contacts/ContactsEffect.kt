package com.mironov.coursework.presentation.contacts

sealed interface ContactsEffect {

    data object Error : ContactsEffect
}