package com.mironov.coursework.presentation.channel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mironov.coursework.domain.entity.Channel
import com.mironov.coursework.domain.entity.Topic
import com.mironov.coursework.navigation.router.ChannelRouter
import com.mironov.coursework.ui.adapter.DelegateItem
import com.mironov.coursework.ui.channels.chenal.ChannelDelegateItem
import com.mironov.coursework.ui.channels.topic.TopicDelegateItem
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class ChannelViewModel @Inject constructor(
    private val router: ChannelRouter
) : ViewModel() {

    private val channelList = mutableListOf(
        Channel(
            id = 1,
            name = "General"
        ),
        Channel(
            id = 2,
            name = "Design"
        ),
        Channel(
            id = 3,
            name = "Development"
        ),
        Channel(
            id = 4,
            name = "PR"
        ),
    )

    private val delegateList: MutableList<DelegateItem> =
        channelList.channelListToDelegateList().toMutableList()

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
        viewModelScope.launch {
            _state.value = ChannelState.Loading
            delay(800)
            _state.value = ChannelState.Content(delegateList.toList())
        }
    }

    fun loadChannel(queryItem: QueryItem, isAllChannels: Boolean) {
        viewModelScope.launch {
            val query = queryItem.query
            val newList = delegateList.filter {
                it is ChannelDelegateItem && it.content().name.startsWith(query)
            }
            _state.value = ChannelState.Content(newList)
        }
    }

    fun showTopics(channelId: Int) {
        val ind = delegateList.indexOfFirst {
            it is ChannelDelegateItem && it.id() == channelId
        }
        val a = (delegateList[ind].content() as Channel).copy(isOpen = true)
        delegateList[ind] = ChannelDelegateItem(a)
        delegateList.addAll(ind + 1, topicList.topicListToListDelegate())
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

    private fun List<Topic>.topicListToListDelegate() = map { it.toDelegate() }

    private fun Channel.toDelegate() = ChannelDelegateItem(this)

    private fun List<Channel>.channelListToDelegateList() = map { it.toDelegate() }
}