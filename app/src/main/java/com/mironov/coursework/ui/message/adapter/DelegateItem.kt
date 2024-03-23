package com.mironov.coursework.ui.message.adapter

interface DelegateItem {

    fun content(): Any

    fun id(): Int

    fun compareToOther(other: DelegateItem): Boolean
}