package com.mironov.coursework.presentation.chat

sealed interface ChatEffect {

    data object ErrorLoadingMessages : ChatEffect

    data object ErrorSendingMessage : ChatEffect

    data object ErrorChangeReaction : ChatEffect
}