package com.mironov.coursework.presentation.chat

import com.mironov.coursework.ui.adapter.DelegateItem

data class ChatState(
    val isLoading: Boolean = false,
    val isNextPageLoading: Boolean = false,
    val content: List<DelegateItem>? = null,
    val isNeedLoadNextPage: Boolean = true,
    val isNeedLoadPrevPage: Boolean = true,
    val topicNameList: List<String>? = null,
    val chatInfo: ChatInfo = ChatInfo(),
)