package com.mironov.coursework.presentation.channel

sealed interface SharedChannelState {

    data object Initial : SharedChannelState

    data class Content(val data: QueryItem) : SharedChannelState
}