package com.mironov.coursework.presentation.chat

import com.mironov.coursework.ui.adapter.DelegateItem

sealed interface ChatState {

    data object Initial : ChatState

    data object Loading : ChatState

    sealed interface Error : ChatState {

        data class SendingError(val cache: List<DelegateItem>) : Error

        data object LoadingError : Error
    }

    data class Content(val data: List<DelegateItem>) : ChatState
}