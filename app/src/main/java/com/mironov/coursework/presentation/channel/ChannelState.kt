package com.mironov.coursework.presentation.channel

import com.mironov.coursework.ui.message.adapter.DelegateItem

sealed interface ChannelState {

    data object Initial : ChannelState

    data class Content(val data: List<DelegateItem>) : ChannelState
}