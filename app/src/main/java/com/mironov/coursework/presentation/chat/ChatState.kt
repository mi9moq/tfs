package com.mironov.coursework.presentation.chat

import com.mironov.coursework.ui.adapter.DelegateItem

data class ChatState(
    val isLoading: Boolean = false,
    val content: List<DelegateItem>? = null
)