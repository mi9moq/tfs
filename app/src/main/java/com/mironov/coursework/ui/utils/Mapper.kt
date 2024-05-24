package com.mironov.coursework.ui.utils

import com.mironov.coursework.domain.entity.Channel
import com.mironov.coursework.domain.entity.Message
import com.mironov.coursework.domain.entity.MessageDate
import com.mironov.coursework.ui.adapter.DelegateItem
import com.mironov.coursework.ui.channels.channel.ChannelDelegateItem
import com.mironov.coursework.ui.channels.create.CreateChannelDelegateItem
import com.mironov.coursework.ui.channels.topic.TopicDelegateItem
import com.mironov.coursework.ui.chat.date.DateDelegateItem
import com.mironov.coursework.ui.chat.received.ReceivedDelegateItem
import com.mironov.coursework.ui.chat.sent.SentDelegateItem
import com.mironov.coursework.ui.chat.topic.MessageTopicDelegateItem

fun List<Message>.groupByDate(isAllTopicsChat: Boolean): List<DelegateItem> {

    val delegateItemList = mutableListOf<DelegateItem>()

    val dates = mutableSetOf<MessageDate>()

    forEach {
        dates.add(MessageDate(it.sendTime))
    }

    var prevTopic: String = first().topicName
    if (isAllTopicsChat)
        delegateItemList.add(MessageTopicDelegateItem(prevTopic))

    dates.forEach {
        delegateItemList.add(DateDelegateItem(it))

        val dateMessages = this.filter { message ->
            message.sendTime == it.date
        }

        dateMessages.forEach { message ->
            if (message.topicName != prevTopic && isAllTopicsChat) {
                prevTopic = message.topicName
                delegateItemList.add(MessageTopicDelegateItem(prevTopic))
            }

            if (message.isMeMessage)
                delegateItemList.add(SentDelegateItem(message))
            else
                delegateItemList.add(ReceivedDelegateItem(message))
        }
    }

    return delegateItemList
}

fun List<Channel>.toDelegates(): List<DelegateItem> {
    val delegateItemList = mutableListOf<DelegateItem>()

    forEach { channel ->
        delegateItemList.add(ChannelDelegateItem(channel))
        channel.topics.forEach { topic ->
            delegateItemList.add(TopicDelegateItem(topic))
        }
    }

    delegateItemList.add(CreateChannelDelegateItem())

    return delegateItemList
}