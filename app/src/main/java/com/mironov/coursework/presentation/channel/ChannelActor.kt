package com.mironov.coursework.presentation.channel

import com.mironov.coursework.domain.entity.Channel
import com.mironov.coursework.domain.entity.Topic
import com.mironov.coursework.domain.repository.Result
import com.mironov.coursework.domain.usecase.GelAllChannelsUseCase
import com.mironov.coursework.domain.usecase.GelSubscribeChannelsUseCase
import com.mironov.coursework.domain.usecase.GetTopicsUseCase
import com.mironov.coursework.ui.adapter.DelegateItem
import com.mironov.coursework.ui.channels.chenal.ChannelDelegateItem
import com.mironov.coursework.ui.channels.topic.TopicDelegateItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import vivid.money.elmslie.core.store.Actor
import javax.inject.Inject

class ChannelActor @Inject constructor(
    private val getAllChannelsUseCase: GelAllChannelsUseCase,
    private val gelSubscribeChannelsUseCase: GelSubscribeChannelsUseCase,
    private val getTopicsUseCase: GetTopicsUseCase
) : Actor<ChannelCommand, ChannelEvent>() {

    private val cache = mutableListOf<DelegateItem>()

    override fun execute(command: ChannelCommand): Flow<ChannelEvent> = flow {
        val event = when (command) {
            ChannelCommand.LoadAllChannels -> loadAllChannels()
            ChannelCommand.LoadSubscribedChannels -> loadSubscribedChannels()
            is ChannelCommand.HideTopics -> hideTopics(command.channelId)
            is ChannelCommand.LoadTopics -> showTopics(command.channel)
        }
        emit(event)
    }

    private suspend fun loadAllChannels(): ChannelEvent.Domain =
        when (val result = getAllChannelsUseCase()) {
            is Result.Failure -> ChannelEvent.Domain.LoadChannelFailure
            is Result.Success -> {
                cache.clear()
                cache.addAll(result.content.channelListToDelegateList())
                ChannelEvent.Domain.LoadChannelsSuccess(
                    result.content.channelListToDelegateList()
                )
            }
        }

    private suspend fun loadSubscribedChannels(): ChannelEvent.Domain =
        when (val result = gelSubscribeChannelsUseCase()) {
            is Result.Failure -> ChannelEvent.Domain.LoadChannelFailure
            is Result.Success -> {
                cache.clear()
                cache.addAll(result.content.channelListToDelegateList())
                ChannelEvent.Domain.LoadChannelsSuccess(
                    result.content.channelListToDelegateList()
                )
            }
        }

    private suspend fun showTopics(channel: Channel): ChannelEvent.Domain =
        when (val result = getTopicsUseCase(channel)) {
            is Result.Failure -> ChannelEvent.Domain.LoadTopicsFailure
            is Result.Success -> {
                val ind = cache.indexOfFirst {
                    it is ChannelDelegateItem && it.id() == channel.id
                }
                val openChannel = (cache[ind].content() as Channel).copy(isOpen = true)
                cache[ind] = ChannelDelegateItem(openChannel)
                cache.addAll(ind + 1, result.content.topicListToListDelegate())
                ChannelEvent.Domain.LoadTopicsSuccess(cache.toList())
            }
        }

    private suspend fun hideTopics(channelId: Int): ChannelEvent.Domain =
        withContext(Dispatchers.Default) {
            val channelInd = cache.indexOfFirst {
                it is ChannelDelegateItem && it.id() == channelId
            }
            val closeChannel = (cache[channelInd].content() as Channel).copy(isOpen = false)
            cache[channelInd] = ChannelDelegateItem(closeChannel)
            var lastTopicIndex = channelInd + 1
            while (cache[lastTopicIndex] is TopicDelegateItem && lastTopicIndex < cache.size - 1)
                lastTopicIndex++
            repeat(lastTopicIndex - channelInd) {
                cache.removeAt(channelInd + 1)
            }
            ChannelEvent.Domain.HideTopicSuccess(cache)
        }

    private fun Topic.toDelegate() = TopicDelegateItem(this)

    private fun List<Topic>.topicListToListDelegate() = map { it.toDelegate() }

    private fun Channel.toDelegate() = ChannelDelegateItem(this)

    private fun List<Channel>.channelListToDelegateList() = map { it.toDelegate() }
}