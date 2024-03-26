package com.mironov.coursework.presentation.message

import com.mironov.coursework.domain.entity.Message

sealed interface MessagesState {

    data object Loading: MessagesState

    data class Content(val data: List<Message>): MessagesState
}