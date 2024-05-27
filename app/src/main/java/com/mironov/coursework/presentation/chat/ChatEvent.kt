package com.mironov.coursework.presentation.chat

import com.mironov.coursework.domain.entity.Message
import com.mironov.coursework.ui.adapter.DelegateItem

sealed interface ChatEvent {

    sealed interface Ui : ChatEvent {

        data object Initial : Ui

        data class Load(val channelName: String, val topicName: String, val id: Int) : Ui

        data class ChangeReaction(
            val messageId: Long,
            val emojiName: String,
            val isSelected: Boolean
        ) : Ui

        data class SendMessage(
            val channelName: String,
            val topicName: String,
            val content: String
        ) : Ui

        data class ChooseReaction(
            val messageId: Long,
            val emojiName: String,
        ) : Ui

        data object OnBackClicked : Ui

        data class ScrollToTop(
            val channelName: String,
            val topicName: String,
        ) : Ui

        data class ScrollToBottom(
            val channelName: String,
            val topicName: String,
        ) : Ui

        data class OnTopicClicked(val chatInfo: ChatInfo) : Ui

        data class OnMessageLongClicked(val message: Message) : Ui

        data class OnEditMessageTopicClicked(val messageId: Long, val oldTopic: String): Ui

        data class SaveNewTopic(val messageId: Long, val newTopic: String): Ui
    }

    sealed interface Domain : ChatEvent {

        data class LoadMessagesSuccess(
            val messages: List<DelegateItem>,
            val isNeedLoadNextPage: Boolean = true,
            val isNeedLoadPrevPage: Boolean = true,
        ) : Domain

        data object LoadMessagesFailure : Domain

        data object EmptyCache : Domain

        data object SendMessageSuccess : Domain

        data object SendMessageFailure : Domain

        data object ChangeReactionSuccess : Domain

        data object ChangeReactionFailure : Domain

        data object LoadTopicFailure : Domain

        data class LoadTopicSuccess(val listTopicName: List<String>) : Domain

        data object Empty : Domain

        data object ChangeTopicSuccess: Domain

        data object ChangeTopicFailure: Domain
    }
}