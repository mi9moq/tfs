package com.mironov.coursework.presentation.contacts

sealed interface ContactsCommand {

    data object Load: ContactsCommand

    data class ApplyFilter(val query: String): ContactsCommand
}