package com.mironov.coursework.presentation.channel

import com.mironov.coursework.navigation.router.ChannelRouter
import com.mironov.coursework.presentation.chat.ChatInfo
import vivid.money.elmslie.core.store.dsl.ScreenDslReducer
import javax.inject.Inject

class ChannelReducer @Inject constructor(
    private val router: ChannelRouter,
) : ScreenDslReducer<ChannelEvent,
        ChannelEvent.Ui,
        ChannelEvent.Domain,
        ChannelState,
        ChannelEffect,
        ChannelCommand>(ChannelEvent.Ui::class, ChannelEvent.Domain::class) {

    override fun Result.internal(event: ChannelEvent.Domain): Any = when (event) {
        is ChannelEvent.Domain.LoadChannelsSuccess -> {
            state {
                copy(isLoading = false, content = event.content)
            }
        }

        ChannelEvent.Domain.LoadChannelFailure -> {
            state {
                copy(isLoading = false)
            }
            effects {
                if (state.content == null)
                    +ChannelEffect.ErrorLoadingChannels
                else
                    +ChannelEffect.ErrorUpdateData
            }
        }

        is ChannelEvent.Domain.LoadTopicsSuccess -> {
            state {
                copy(isLoading = false, content = event.content)
            }
        }

        ChannelEvent.Domain.LoadTopicsFailure -> {
            state {
                copy(isLoading = false, content = null)
            }
            effects {
                +ChannelEffect.ErrorLoadingTopics
            }
        }

        is ChannelEvent.Domain.HideTopicSuccess -> {
            state {
                copy(isLoading = false, content = event.content)
            }
        }

        is ChannelEvent.Domain.FilterSuccess -> {
            state {
                copy(content = event.content)
            }
        }

        ChannelEvent.Domain.EmptyChannelCache -> {
            state {
                copy(isLoading = true, content = null)
            }
        }

        ChannelEvent.Domain.CreateChannelFailure -> {
            effects {
                +ChannelEffect.ErrorCreateChannel
            }
        }


        ChannelEvent.Domain.CreateChannelSuccess -> {
            commands {
                +ChannelCommand.LoadSubscribedChannels
            }
        }

        ChannelEvent.Domain.EmptyEvent -> Unit
    }

    override fun Result.ui(event: ChannelEvent.Ui): Any = when (event) {
        ChannelEvent.Ui.InitialAll -> {
            state {
                copy(isLoading = true)
            }
            commands {
                +ChannelCommand.LoadAllChannelsCache
                +ChannelCommand.LoadAllChannels
            }
        }

        ChannelEvent.Ui.InitialSubscribed -> {
            state {
                copy(isLoading = true)
            }
            commands {
                +ChannelCommand.LoadSubscribedChannelsCache
                +ChannelCommand.LoadSubscribedChannels
            }
        }

        is ChannelEvent.Ui.ShowTopic -> {
            commands {
                +ChannelCommand.LoadTopicsCache(event.channel)
                +ChannelCommand.LoadTopics(event.channel)
            }
        }

        is ChannelEvent.Ui.HideTopic -> {
            commands {
                +ChannelCommand.HideTopics(event.channelId)
            }
        }

        is ChannelEvent.Ui.ChangeFilter -> {
            commands {
                +ChannelCommand.ApplyFilter(event.queryItem)
            }
        }

        is ChannelEvent.Ui.OnTopicClicked -> {
            router.openChat(
                ChatInfo(
                    channelName = event.topic.parentChannelName,
                    topicName = event.topic.name
                )
            )
        }

        is ChannelEvent.Ui.OnChannelClicked ->
            router.openChat(
                ChatInfo(
                    channelName = event.channelName,
                    channelId = event.channelId
                )
            )

        is ChannelEvent.Ui.CreateChannel -> {
            commands {
                +ChannelCommand.CreateChannel(event.name, event.description)
            }
        }

        ChannelEvent.Ui.ReloadAll -> {
            state {
                copy(isLoading = true)
            }
            commands {
                +ChannelCommand.LoadAllChannels
            }
        }
        ChannelEvent.Ui.ReloadSubscribed -> {
            state {
                copy(isLoading = true)
            }
            commands {
                +ChannelCommand.LoadSubscribedChannels
            }
        }
    }
}