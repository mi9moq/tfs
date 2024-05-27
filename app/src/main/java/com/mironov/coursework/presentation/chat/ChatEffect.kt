package com.mironov.coursework.presentation.chat

sealed interface ChatEffect {

    data object ErrorLoadingMessages : ChatEffect

    data object ErrorSendingMessage : ChatEffect

    data object ErrorChangeReaction : ChatEffect

    data class ShowMessageActionDialog(
        val messageId: Long,
        val isContentEditable: Boolean,
        val isTopicEditable: Boolean,
        val canDelete: Boolean,
    ) : ChatEffect
}