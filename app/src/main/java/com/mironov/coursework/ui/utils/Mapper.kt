package com.mironov.coursework.ui.utils

import com.mironov.coursework.domain.entity.Message
import com.mironov.coursework.domain.entity.MessageDate
import com.mironov.coursework.ui.adapter.DelegateItem
import com.mironov.coursework.ui.chat.date.DateDelegateItem
import com.mironov.coursework.ui.chat.received.ReceivedDelegateItem
import com.mironov.coursework.ui.chat.sent.SentDelegateItem

fun List<Message>.groupByDate(): List<DelegateItem> {

    val delegateItemList = mutableListOf<DelegateItem>()

    val dates = mutableSetOf<MessageDate>()

    this.forEach {
        dates.add(MessageDate(it.sendTime))
    }

    dates.forEach {
        delegateItemList.add(DateDelegateItem(it))

        val dateMessages = this.filter {message ->
            message.sendTime == it.date
        }

        dateMessages.forEach {message ->
            if (message.isMeMessage) {
                delegateItemList.add(SentDelegateItem(message))
            } else {
                delegateItemList.add(ReceivedDelegateItem(message))
            }
        }
    }

    return delegateItemList
}