package com.mironov.coursework.ui.channels.channel

import com.mironov.coursework.domain.entity.Channel
import com.mironov.coursework.ui.adapter.DelegateItem

class ChannelDelegateItem (private val channel: Channel): DelegateItem {

    override fun content(): Channel =
        channel

    override fun id(): Int =
        channel.id

    override fun compareToOther(other: DelegateItem): Boolean =
        (other as ChannelDelegateItem).content() == channel
}