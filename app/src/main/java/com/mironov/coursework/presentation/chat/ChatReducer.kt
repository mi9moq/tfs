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
                +ChatEffect.ErrorChangeReaction
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
                copy(
                    isLoading = false,
                    content = event.messages,
                    isNextPageLoading = false,
                )
            }
        }

        is ChatEvent.Domain.SendMessageFailure -> {
            state {
                copy(isLoading = false)
            }
            effects {
                +ChatEffect.ErrorSendingMessage
            }
        }

        is ChatEvent.Domain.SendMessageSuccess -> {
            state {
                copy(isLoading = false)
            }
        }

        ChatEvent.Domain.Empty -> {}
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

        is ChatEvent.Ui.ChooseReaction -> {
            commands {
                +ChatCommand.ChooseReaction(event.messageId, event.emojiName)
            }
        }

        is ChatEvent.Ui.ScrollToBottom -> {
            commands {
                +ChatCommand.LoadNextMessages(event.channelName, event.topicName)
            }
            state {
                copy(isNextPageLoading = true)
            }
        }

        is ChatEvent.Ui.ScrollToTop -> {
            commands {
                +ChatCommand.LoadPrevMessages(event.channelName, event.topicName)
            }
            state {
                copy(isNextPageLoading = true)
            }
        }
    }
}