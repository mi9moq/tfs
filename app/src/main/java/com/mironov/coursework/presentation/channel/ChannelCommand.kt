package com.mironov.coursework.presentation.channel

import com.mironov.coursework.domain.entity.Channel

sealed interface ChannelCommand {

    data object LoadAllChannels : ChannelCommand

    data object LoadAllChannelsCache : ChannelCommand

    data object LoadSubscribedChannels : ChannelCommand

    data object LoadSubscribedChannelsCache : ChannelCommand

    data class LoadTopics(val channel: Channel) : ChannelCommand

    data class HideTopics(val channelId: Int) : ChannelCommand

    data class ApplyFilter(val queryItem: QueryItem) : ChannelCommand
}