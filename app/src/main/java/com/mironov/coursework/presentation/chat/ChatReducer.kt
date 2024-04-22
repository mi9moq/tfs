package com.mironov.coursework.presentation.chat

import com.mironov.coursework.navigation.router.ChatRouter
import vivid.money.elmslie.core.store.dsl.ScreenDslReducer
import javax.inject.Inject

class ChatReducer @Inject constructor(
    private val router: ChatRouter,
) : ScreenDslReducer<ChatEvent,
        ChatEvent.Ui,
        ChatEvent.Domain,
        ChatState,
        ChatEffect,
        ChatCommand>(ChatEvent.Ui::class, ChatEvent.Domain::class) {

    override fun Result.internal(event: ChatEvent.Domain): Any = when (event) {
        is ChatEvent.Domain.ChangeReactionFailure -> {
            state {
                copy(isLoading = false)
            }
            effects {
                +ChatEffect.ErrorChangeReaction(event.messages)
            }
        }

        is ChatEvent.Domain.ChangeReactionSuccess -> {
            state {
                copy(isLoading = false)
            }
        }

        is ChatEvent.Domain.LoadMessagesFailure -> {
            state {
                copy(isLoading = false)
            }
            effects {
                +ChatEffect.ErrorLoadingMessages
            }
        }

        is ChatEvent.Domain.LoadMessagesSuccess -> {
            state {
                copy(isLoading = false, content = event.messages)
            }
        }

        is ChatEvent.Domain.SendMessageFailure -> {
            state {
                copy(isLoading = false)
            }
            effects {
                +ChatEffect.ErrorSendingMessage(event.messages)
            }
        }

        is ChatEvent.Domain.SendMessageSuccess -> {
            state {
                copy(isLoading = false)
            }
        }
    }

    override fun Result.ui(event: ChatEvent.Ui): Any = when (event) {
        ChatEvent.Ui.Initial -> {}
        is ChatEvent.Ui.Load -> {
            state {
                copy(isLoading = true)
            }
            commands {
                +ChatCommand.LoadMessage(event.channelName, event.topicName)
            }
        }

        is ChatEvent.Ui.ChangeReaction -> {
            commands {
                +ChatCommand.ChangeReaction(event.messageId, event.emojiName, event.isSelected)
            }
        }

        ChatEvent.Ui.OnBackClicked -> {
            router.back()
        }

        is ChatEvent.Ui.SendMessage -> {
            commands {
                +ChatCommand.SendMessage(event.channelName, event.topicName, event.content)
            }
        }
    }
}