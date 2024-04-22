package com.mironov.coursework.presentation.chat

import com.mironov.coursework.ui.adapter.DelegateItem

sealed interface ChatEffect {

    data object ErrorLoadingMessages : ChatEffect

    data class ErrorSendingMessage(val messages: List<DelegateItem>) : ChatEffect

    data class ErrorChangeReaction(val messages: List<DelegateItem>) : ChatEffect
}