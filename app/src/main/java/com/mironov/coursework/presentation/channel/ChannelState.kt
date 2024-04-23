package com.mironov.coursework.presentation.channel

import com.mironov.coursework.ui.adapter.DelegateItem

data class ChannelState(
    val isLoading: Boolean = false,
    val content: List<DelegateItem>? = null
)
