package com.mironov.coursework.ui.channels.create

import com.mironov.coursework.ui.adapter.DelegateItem

class CreateChannelDelegateItem : DelegateItem {

    override fun content(): Any = value

    override fun id(): Int = value.hashCode()

    override fun compareToOther(other: DelegateItem): Boolean = true

    private companion object {

        private val value = Any()
    }
}