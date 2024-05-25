package com.mironov.coursework.presentation.channel

import com.mironov.coursework.domain.entity.Channel
import com.mironov.coursework.domain.entity.Topic
import com.mironov.coursework.ui.adapter.DelegateItem

sealed interface ChannelEvent {

    sealed interface Ui : ChannelEvent {

        data object InitialAll : Ui

        data object InitialSubscribed : Ui

        data class ShowTopic(val channel: Channel) : Ui

        data class HideTopic(val channelId: Int) : Ui

        data class OnTopicClicked(val topic: Topic) : Ui

        data class OnChannelClicked(val channelName: String) : Ui

        data class ChangeFilter(val queryItem: QueryItem): Ui

        data class CreateChannel(val name: String, val description: String): Ui
    }

    sealed interface Domain : ChannelEvent {

        data class LoadChannelsSuccess(val content: List<DelegateItem>) : Domain

        data object LoadChannelFailure : Domain

        data class LoadTopicsSuccess(val content: List<DelegateItem>) : Domain

        data object LoadTopicsFailure : Domain

        data class HideTopicSuccess(val content: List<DelegateItem>) : Domain

        data class FilterSuccess(
            val content: List<DelegateItem>,
            val query: String
        ): Domain

        data object EmptyCache: Domain

        data object CreateChannelSuccess: Domain

        data object CreateChannelFailure: Domain
    }
}