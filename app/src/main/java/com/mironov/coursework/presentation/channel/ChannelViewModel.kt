package com.mironov.coursework.presentation.channel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mironov.coursework.domain.entity.Channel
import com.mironov.coursework.domain.entity.Topic
import com.mironov.coursework.navigation.router.ChannelRouter
import com.mironov.coursework.ui.channels.chenal.ChannelDelegateItem
import com.mironov.coursework.ui.channels.topic.TopicDelegateItem
import com.mironov.coursework.ui.adapter.DelegateItem
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class ChannelViewModel @Inject constructor(
    private val router: ChannelRouter
) : ViewModel() {

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

    private val topicList = listOf(
        Topic(
            id = 1,
            name = "Test",
            messageCount = 243,
        ),
        Topic(
            id = 2,
            name = "Bruh",
            messageCount = 64,
        ),
    )

    private val _state = MutableStateFlow<ChannelState>(ChannelState.Initial)
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch{
            _state.value = ChannelState.Loading
            delay(3500)
            _state.value = ChannelState.Content(delegateList.toList())
        }
    }

    fun showTopics(channelId: Int) {
        val ind = delegateList.indexOfFirst {
            it is ChannelDelegateItem && it.id() == channelId
        }
        val a = (delegateList[ind].content() as Channel).copy(isOpen = true)
        delegateList[ind] = ChannelDelegateItem(a)
        delegateList.addAll(ind + 1, topicList.toListDelegate())
        _state.value = ChannelState.Content(delegateList.toList())
    }

    fun hideTopics(channelId: Int) {
        val ind = delegateList.indexOfFirst {
            it is ChannelDelegateItem && it.id() == channelId
        }
        val a = (delegateList[ind].content() as Channel).copy(isOpen = false)
        delegateList[ind] = ChannelDelegateItem(a)
        delegateList.removeAt(ind + 1)
        delegateList.removeAt(ind + 1)
        _state.value = ChannelState.Content(delegateList.toList())
    }

    fun openChat(chatId: Int) {
        router.openChat(chatId)
    }

    private fun Topic.toDelegate() = TopicDelegateItem(this)

    private fun List<Topic>.toListDelegate() = this.map { it.toDelegate() }
}