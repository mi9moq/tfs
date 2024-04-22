package com.mironov.coursework.presentation.chat

import com.mironov.coursework.ui.adapter.DelegateItem

sealed interface ChatEvent {

    sealed interface Ui : ChatEvent {

        data object Initial : Ui

        data class Load(val channelName: String, val topicName: String) : Ui

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

        data object OnBackClicked: Ui
    }

    sealed interface Domain : ChatEvent {

        data class LoadMessagesSuccess(val messages: List<DelegateItem>) : Domain

        data object LoadMessagesFailure : Domain

        data class SendMessageSuccess(val messages: List<DelegateItem>) : Domain

        data class SendMessageFailure(val messages: List<DelegateItem>) : Domain

        data class ChangeReactionSuccess(val messages: List<DelegateItem>) : Domain

        data class ChangeReactionFailure(val messages: List<DelegateItem>) : Domain
    }
}