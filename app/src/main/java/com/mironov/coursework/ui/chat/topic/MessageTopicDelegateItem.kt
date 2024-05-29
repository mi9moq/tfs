package com.mironov.coursework.ui.chat.topic

import com.mironov.coursework.ui.adapter.DelegateItem

class MessageTopicDelegateItem(private val topicName: String): DelegateItem {

    override fun content(): String = topicName

    override fun id(): Int = topicName.hashCode()

    override fun compareToOther(other: DelegateItem): Boolean =
        (other as MessageTopicDelegateItem).topicName.equals(topicName, ignoreCase = true)
}