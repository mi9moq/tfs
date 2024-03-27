package com.mironov.coursework.presentation.chat

import com.mironov.coursework.domain.entity.Message

sealed interface ChatState {

    data object Loading: ChatState

    data class Content(val data: List<Message>): ChatState
}