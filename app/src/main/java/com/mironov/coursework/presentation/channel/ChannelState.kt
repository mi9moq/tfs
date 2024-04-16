package com.mironov.coursework.presentation.channel

import com.mironov.coursework.ui.adapter.DelegateItem

sealed interface ChannelState {

    data object Initial : ChannelState

    data object Loading : ChannelState

    data object Error : ChannelState

    data class Content(val data: List<DelegateItem>) : ChannelState
}