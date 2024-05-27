package com.mironov.coursework.presentation.chat

import com.mironov.coursework.domain.entity.Message

sealed interface ChatEffect {

    data object ErrorLoadingMessages : ChatEffect

    data object ErrorSendingMessage : ChatEffect

    data object ErrorChangeReaction : ChatEffect

    data class ShowMessageActionDialog(
        val message: Message,
        val isContentEditable: Boolean,
        val isTopicEditable: Boolean,
        val canDelete: Boolean,
    ) : ChatEffect

    data class ShowEditTopicDialog(val messageId: Long, val oldTopic: String) : ChatEffect
}