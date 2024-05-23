package com.mironov.coursework.presentation.channel

import com.mironov.coursework.navigation.router.ChannelRouter
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
                copy(isLoading = false, content = null)
            }
            effects {
                +ChannelEffect.ErrorLoadingChannels
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
                if (event.content.isEmpty() && event.query.isEmpty())
                    copy(isLoading = true)
                else
                    copy(isLoading = false, content = event.content)
            }
        }

        ChannelEvent.Domain.EmptyCache -> {
            state {
                copy(isLoading = true, content = null)
            }
        }
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
            router.openChat(event.topic.parentChannelName, event.topic.name)
        }

        is ChannelEvent.Ui.OnChannelClicked ->
            router.openChat(event.channelName, NO_TOPIC)
    }

    private companion object {

        private const val NO_TOPIC = ""
    }
}