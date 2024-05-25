package com.mironov.coursework.presentation.channel

sealed interface ChannelEffect {

    data object ErrorLoadingChannels: ChannelEffect

    data object ErrorLoadingTopics: ChannelEffect

    data object ErrorCreateChannel: ChannelEffect
}