package com.mironov.coursework.ui.message.received

import com.mironov.coursework.domain.entity.Message
import com.mironov.coursework.ui.message.adapter.DelegateItem

class ReceivedDelegateItem(private val message: Message) : DelegateItem {

    override fun content(): Message = message

    override fun id(): Int = message.id

    override fun compareToOther(other: DelegateItem): Boolean =
        (other as ReceivedDelegateItem).content() == message
}