package com.mironov.coursework.ui.chat.date

import com.mironov.coursework.domain.entity.MessageDate
import com.mironov.coursework.ui.adapter.DelegateItem

class DateDelegateItem(private val date: MessageDate) : DelegateItem {

    override fun content(): MessageDate = date

    override fun id(): Int = date.hashCode()

    override fun compareToOther(other: DelegateItem): Boolean =
        (other as DateDelegateItem).content() == date
}