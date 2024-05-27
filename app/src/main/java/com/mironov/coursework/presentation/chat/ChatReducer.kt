package com.mironov.coursework.presentation.chat

import com.mironov.coursework.domain.entity.Message
import com.mironov.coursework.domain.utils.MILLIS_IN_SECONDS
import com.mironov.coursework.domain.utils.SECONDS_IN_MINUTES
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
                    isNeedLoadNextPage = event.isNeedLoadNextPage,
                    isNeedLoadPrevPage = event.isNeedLoadPrevPage
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

        ChatEvent.Domain.EmptyCache -> {
            state {
                copy(isLoading = true)
            }
        }

        ChatEvent.Domain.LoadTopicFailure -> Unit //TODO()

        is ChatEvent.Domain.LoadTopicSuccess -> {
            state {
                copy(topicNameList = event.listTopicName)
            }
        }

        ChatEvent.Domain.Empty -> Unit

        ChatEvent.Domain.ChangeTopicFailure -> Unit //TODO() показать оишбку

        ChatEvent.Domain.ChangeTopicSuccess -> Unit //TODO()
    }

    override fun Result.ui(event: ChatEvent.Ui): Any = when (event) {
        ChatEvent.Ui.Initial -> {}
        is ChatEvent.Ui.Load -> {
            commands {
                +ChatCommand.LoadMessageCache(event.channelName, event.topicName)
                +ChatCommand.LoadMessage(event.channelName, event.topicName)
                +ChatCommand.LoadExistingTopics(event.id, event.channelName)
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

        is ChatEvent.Ui.OnTopicClicked -> router.showTopic(event.chatInfo)

        is ChatEvent.Ui.OnMessageLongClicked -> {
            val isContentEditable = event.message.isContentEditable()
            val isTopicEditable = event.message.isTopicEditable()
            val canDelete = event.message.canDelete()
            effects {
                +ChatEffect.ShowMessageActionDialog(
                    message = event.message,
                    isContentEditable = isContentEditable,
                    isTopicEditable = isTopicEditable,
                    canDelete = canDelete
                )
            }
        }

        is ChatEvent.Ui.OnEditMessageTopicClicked -> {
            effects {
                +ChatEffect.ShowEditTopicDialog(event.messageId, event.oldTopic)
            }
        }

        is ChatEvent.Ui.SaveNewTopic -> {
            commands {
                +ChatCommand.ChangeTopic(event.messageId,event.newTopic)
            }
        }
    }

    private fun Message.isContentEditable(): Boolean {
        val timesLeft =
            (System.currentTimeMillis() / MILLIS_IN_SECONDS - sendTime) / SECONDS_IN_MINUTES
        return isMeMessage && timesLeft < Message.MESSAGE_CONTENT_EDITABLE_MINUTES
    }

    private fun Message.isTopicEditable(): Boolean {
        val timesLeft =
            (System.currentTimeMillis() / MILLIS_IN_SECONDS - sendTime) / SECONDS_IN_MINUTES
        return isMeMessage && timesLeft < Message.MESSAGE_TOPIC_EDITABLE_MINUTES
    }

    private fun Message.canDelete(): Boolean {
        val timesLeft =
            (System.currentTimeMillis() / MILLIS_IN_SECONDS - sendTime) / SECONDS_IN_MINUTES
        return isMeMessage && timesLeft < Message.MESSAGE_CAN_DELETED_MINUTES
    }
}