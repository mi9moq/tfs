package com.mironov.coursework.ui.chat.sent

import com.mironov.coursework.domain.entity.Message
import com.mironov.coursework.ui.adapter.DelegateItem

class SentDelegateItem(private val message: Message) : DelegateItem {

    override fun content(): Message = message

    override fun id(): Int = message.id.toInt()

    override fun compareToOther(other: DelegateItem): Boolean =
        (other as SentDelegateItem).content() == message
}