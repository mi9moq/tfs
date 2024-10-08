package com.mironov.coursework.presentation.channel

import com.mironov.coursework.di.app.annotation.DefaultDispatcher
import com.mironov.coursework.domain.entity.Channel
import com.mironov.coursework.domain.repository.Result
import com.mironov.coursework.domain.usecase.CreateChannelUseCase
import com.mironov.coursework.domain.usecase.GelAllChannelsCacheUseCase
import com.mironov.coursework.domain.usecase.GelAllChannelsUseCase
import com.mironov.coursework.domain.usecase.GelSubscribeChannelsCacheUseCase
import com.mironov.coursework.domain.usecase.GetSubscribeChannelsUseCase
import com.mironov.coursework.domain.usecase.GetTopicsCacheUseCase
import com.mironov.coursework.domain.usecase.GetTopicsUseCase
import com.mironov.coursework.ui.utils.toDelegates
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import vivid.money.elmslie.core.store.Actor
import javax.inject.Inject

class ChannelActor @Inject constructor(
    @DefaultDispatcher private val dispatcher: CoroutineDispatcher,
    private val getAllChannelsUseCase: GelAllChannelsUseCase,
    private val getAllChannelsCacheUseCase: GelAllChannelsCacheUseCase,
    private val getSubscribeChannelsUseCase: GetSubscribeChannelsUseCase,
    private val gelSubscribeChannelsCacheUseCase: GelSubscribeChannelsCacheUseCase,
    private val getTopicsUseCase: GetTopicsUseCase,
    private val getTopicsCacheUseCase: GetTopicsCacheUseCase,
    private val createChannelUseCase: CreateChannelUseCase,
) : Actor<ChannelCommand, ChannelEvent>() {

    private val cacheChannel = mutableListOf<Channel>()

    override fun execute(command: ChannelCommand): Flow<ChannelEvent> = flow {
        val event = when (command) {
            ChannelCommand.LoadAllChannels -> loadAllChannels()
            ChannelCommand.LoadSubscribedChannels -> loadSubscribedChannels()
            is ChannelCommand.HideTopics -> hideTopics(command.channelId)
            is ChannelCommand.LoadTopics -> showTopics(command.channel)
            is ChannelCommand.ApplyFilter -> applyFilter(command.queryItem)
            ChannelCommand.LoadAllChannelsCache -> loadAllChannelsCache()
            ChannelCommand.LoadSubscribedChannelsCache -> loadSubscribedChannelsCache()
            is ChannelCommand.LoadTopicsCache -> showTopicsCache(command.channel)
            is ChannelCommand.CreateChannel -> createChannel(command.name, command.description)
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

    private suspend fun loadAllChannelsCache(): ChannelEvent.Domain =
        when (val result = getAllChannelsCacheUseCase()) {
            is Result.Failure -> ChannelEvent.Domain.LoadChannelFailure
            is Result.Success -> {
                if (result.content.isEmpty()) {
                    ChannelEvent.Domain.EmptyChannelCache
                } else {
                    cacheChannel.clear()
                    cacheChannel.addAll(result.content)
                    ChannelEvent.Domain.LoadChannelsSuccess(
                        result.content.toDelegates()
                    )
                }
            }
        }

    private suspend fun loadSubscribedChannels(): ChannelEvent.Domain =
        when (val result = getSubscribeChannelsUseCase()) {
            is Result.Failure -> ChannelEvent.Domain.LoadChannelFailure
            is Result.Success -> {
                cacheChannel.clear()
                cacheChannel.addAll(result.content)
                ChannelEvent.Domain.LoadChannelsSuccess(
                    result.content.toDelegates()
                )
            }
        }

    private suspend fun loadSubscribedChannelsCache(): ChannelEvent.Domain =
        when (val result = gelSubscribeChannelsCacheUseCase()) {
            is Result.Failure -> ChannelEvent.Domain.LoadChannelFailure
            is Result.Success -> {
                if (result.content.isEmpty()) {
                    ChannelEvent.Domain.EmptyChannelCache
                } else {
                    cacheChannel.clear()
                    cacheChannel.addAll(result.content)
                    ChannelEvent.Domain.LoadChannelsSuccess(
                        result.content.toDelegates()
                    )
                }
            }
        }

    private suspend fun showTopics(channel: Channel): ChannelEvent.Domain =
        when (val result = getTopicsUseCase(channel.id, channel.name)) {
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

    private suspend fun showTopicsCache(channel: Channel): ChannelEvent.Domain =
        when (val result = getTopicsCacheUseCase(channel)) {
            is Result.Failure -> ChannelEvent.Domain.LoadTopicsFailure
            is Result.Success -> {
                if (result.content.isEmpty()) {
                    ChannelEvent.Domain.EmptyEvent
                } else {
                    val ind = cacheChannel.indexOfFirst {
                        it.id == channel.id
                    }
                    val newChannel =
                        (cacheChannel[ind]).copy(isOpen = true, topics = result.content)
                    cacheChannel[ind] = newChannel
                    ChannelEvent.Domain.LoadTopicsSuccess(cacheChannel.toDelegates())
                }
            }
        }

    private suspend fun hideTopics(channelId: Int): ChannelEvent.Domain =
        withContext(dispatcher) {
            val channelInd = cacheChannel.indexOfFirst {
                it.id == channelId
            }
            val newChannel = cacheChannel[channelInd].copy(isOpen = false, topics = emptyList())
            cacheChannel[channelInd] = newChannel
            ChannelEvent.Domain.HideTopicSuccess(cacheChannel.toDelegates())
        }

    private suspend fun applyFilter(queryItem: QueryItem): ChannelEvent.Domain =
        withContext(dispatcher) {
            if (cacheChannel.isEmpty()) return@withContext ChannelEvent.Domain.EmptyEvent
            val channels = cacheChannel.filter { channel ->
                channel.name.startsWith(queryItem.query)
            }
            ChannelEvent.Domain.FilterSuccess(channels.toDelegates(), queryItem.query)
        }

    private suspend fun createChannel(name: String, description: String): ChannelEvent =
        when (createChannelUseCase(name, description)) {
            is Result.Failure -> ChannelEvent.Domain.CreateChannelFailure
            is Result.Success -> ChannelEvent.Domain.CreateChannelSuccess
        }
}