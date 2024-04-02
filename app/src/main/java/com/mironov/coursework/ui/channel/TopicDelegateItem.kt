package com.mironov.coursework.ui.channel

import com.mironov.coursework.domain.entity.Topic
import com.mironov.coursework.ui.message.adapter.DelegateItem

class TopicDelegateItem (private val topic: Topic): DelegateItem {

    override fun content(): Topic =
        topic

    override fun id(): Int =
        topic.id

    override fun compareToOther(other: DelegateItem): Boolean =
        (other as TopicDelegateItem).content() == topic
}