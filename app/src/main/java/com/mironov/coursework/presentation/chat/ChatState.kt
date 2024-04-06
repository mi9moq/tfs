package com.mironov.coursework.presentation.chat

import com.mironov.coursework.ui.adapter.DelegateItem

sealed interface ChatState {

    data object Initial : ChatState

    data object Loading : ChatState

    data class Content(val data: List<DelegateItem>) : ChatState
}