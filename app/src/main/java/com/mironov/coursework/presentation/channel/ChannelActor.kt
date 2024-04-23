package com.mironov.coursework.presentation.channel

import com.mironov.coursework.domain.entity.Channel
import com.mironov.coursework.domain.repository.Result
import com.mironov.coursework.domain.usecase.GelAllChannelsUseCase
import com.mironov.coursework.domain.usecase.GelSubscribeChannelsUseCase
import com.mironov.coursework.domain.usecase.GetTopicsUseCase
import com.mironov.coursework.ui.utils.toDelegates
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

    private val cacheChannel = mutableListOf<Channel>()

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
                cacheChannel.clear()
                cacheChannel.addAll(result.content)
                ChannelEvent.Domain.LoadChannelsSuccess(
                    result.content.toDelegates()
                )
            }
        }

    private suspend fun loadSubscribedChannels(): ChannelEvent.Domain =
        when (val result = gelSubscribeChannelsUseCase()) {
            is Result.Failure -> ChannelEvent.Domain.LoadChannelFailure
            is Result.Success -> {
                cacheChannel.clear()
                cacheChannel.addAll(result.content)
                ChannelEvent.Domain.LoadChannelsSuccess(
                    result.content.toDelegates()
                )
            }
        }

    private suspend fun showTopics(channel: Channel): ChannelEvent.Domain =
        when (val result = getTopicsUseCase(channel)) {
            is Result.Failure -> ChannelEvent.Domain.LoadTopicsFailure
            is Result.Success -> {
                val ind = cacheChannel.indexOfFirst {
                    it.id == channel.id
                }
                val newChannel = (cacheChannel[ind]).copy(isOpen = true, topics = result.content)
                cacheChannel[ind] = newChannel
                ChannelEvent.Domain.LoadTopicsSuccess(cacheChannel.toDelegates())
            }
        }

    private suspend fun hideTopics(channelId: Int): ChannelEvent.Domain =
        withContext(Dispatchers.Default) {
            val channelInd = cacheChannel.indexOfFirst {
                it.id == channelId
            }
            val newChannel = cacheChannel[channelInd].copy(isOpen = false, topics = emptyList())
            cacheChannel[channelInd] = newChannel
            ChannelEvent.Domain.HideTopicSuccess(cacheChannel.toDelegates())
        }
}