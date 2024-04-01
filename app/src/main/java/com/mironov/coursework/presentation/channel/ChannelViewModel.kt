package com.mironov.coursework.presentation.channel

import androidx.lifecycle.ViewModel
import com.mironov.coursework.domain.entity.Channel
import com.mironov.coursework.ui.channel.ChannelDelegateItem
import com.mironov.coursework.ui.message.adapter.DelegateItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class ChannelViewModel : ViewModel() {

    private val delegateList = mutableListOf<DelegateItem>(
        ChannelDelegateItem(
            Channel(
                id = 1,
                name = "General"
            )
        ),
        ChannelDelegateItem(
            Channel(
                id = 2,
                name = "Design"
            )
        ),
        ChannelDelegateItem(
            Channel(
                id = 3,
                name = "Development"
            )
        ),
        ChannelDelegateItem(
            Channel(
                id = 4,
                name = "PR"
            )
        ),
    )

    private val _state = MutableStateFlow<ChannelState>(ChannelState.Initial)
    val state = _state.asStateFlow()

    init {
        _state.value = ChannelState.Content(delegateList.toList())
    }
}