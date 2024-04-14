package com.mironov.coursework.presentation.channel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mironov.coursework.data.mapper.toListChannel
import com.mironov.coursework.data.mapper.toListTopic
import com.mironov.coursework.data.network.api.ZulipApi
import com.mironov.coursework.domain.entity.Channel
import com.mironov.coursework.domain.entity.Topic
import com.mironov.coursework.navigation.router.ChannelRouter
import com.mironov.coursework.ui.channels.chenal.ChannelDelegateItem
import com.mironov.coursework.ui.channels.topic.TopicDelegateItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class ChannelViewModel @Inject constructor(
    private val router: ChannelRouter,
    private val api: ZulipApi
) : ViewModel() {

    private val _state = MutableStateFlow<ChannelState>(ChannelState.Initial)
    val state = _state.asStateFlow()

    private val channelCache = mutableListOf<Channel>()

    fun loadAllChannel() {
        viewModelScope.launch {
            _state.value = ChannelState.Loading
            val channels = api.getAllStreams().streams.toListChannel()
            channelCache.addAll(channels)
            _state.value = ChannelState.Content(channels.channelListToDelegateList())
        }
    }

    fun loadSubscribedChannel() {
        viewModelScope.launch {
            _state.value = ChannelState.Loading
            val channels = api
                .getSubscribedStreams()
                .streams
                .toListChannel()
            channelCache.addAll(channels)
            _state.value = ChannelState.Content(channels.channelListToDelegateList())
        }
    }


    fun showTopics(channel: Channel) {
        viewModelScope.launch {
            val cache = (_state.value as ChannelState.Content).data.toMutableList()
            val ind = cache.indexOfFirst {
                it is ChannelDelegateItem && it.id() == channel.id
            }
            val openChannel = (cache[ind].content() as Channel).copy(isOpen = true)
            cache[ind] = ChannelDelegateItem(openChannel)
            val topics = api.getTopics(channel.id).toListTopic(channel.name)
            cache.addAll(ind + 1, topics.topicListToListDelegate())
            _state.value = ChannelState.Content(cache.toList())
        }
    }

    fun hideTopics(channelId: Int) {
        viewModelScope.launch {
            val topicsCount = api.getTopics(channelId).topics.size
            val cache = (_state.value as ChannelState.Content).data.toMutableList()
            val ind = cache.indexOfFirst {
                it is ChannelDelegateItem && it.id() == channelId
            }
            val closeChannel = (cache[ind].content() as Channel).copy(isOpen = false)
            cache[ind] = ChannelDelegateItem(closeChannel)
            repeat(topicsCount) {
                cache.removeAt(ind + 1)
            }
            _state.value = ChannelState.Content(cache.toList())
        }
    }

    fun filterChannels(queryItem: QueryItem) {
        if (queryItem.query.isBlank()) return
        val channels = channelCache.filter {
            it.name.startsWith(queryItem.query)
        }
        _state.value = ChannelState.Content(channels.channelListToDelegateList())
    }

    fun openChat(topic: Topic) {
        router.openChat(topic.parentChannelName,topic.name)
    }

    private fun Topic.toDelegate() = TopicDelegateItem(this)

    private fun List<Topic>.topicListToListDelegate() = map { it.toDelegate() }

    private fun Channel.toDelegate() = ChannelDelegateItem(this)

    private fun List<Channel>.channelListToDelegateList() = map { it.toDelegate() }
}