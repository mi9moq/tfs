package com.mironov.coursework.presentation.chat

import com.mironov.coursework.domain.repository.Result
import com.mironov.coursework.domain.usecase.AddReactionUseCase
import com.mironov.coursework.domain.usecase.GetMessageByIdUseCase
import com.mironov.coursework.domain.usecase.GetMessagesCacheUseCase
import com.mironov.coursework.domain.usecase.GetMessagesUseCase
import com.mironov.coursework.domain.usecase.GetNextMessagesUseCase
import com.mironov.coursework.domain.usecase.GetPrevMessagesUseCase
import com.mironov.coursework.domain.usecase.RemoveReactionUseCase
import com.mironov.coursework.domain.usecase.SendMessageUseCase
import com.mironov.coursework.ui.utils.groupByDate
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import vivid.money.elmslie.core.store.Actor
import javax.inject.Inject

class ChatActor @Inject constructor(
    private val sendMessageUseCase: SendMessageUseCase,
    private val getMessagesUseCase: GetMessagesUseCase,
    private val addReactionUseCase: AddReactionUseCase,
    private val removeReactionUseCase: RemoveReactionUseCase,
    private val getMessageByIdUseCase: GetMessageByIdUseCase,
    private val getNextMessagesUseCase: GetNextMessagesUseCase,
    private val getPrevMessagesUseCase: GetPrevMessagesUseCase,
    private val getMessagesCacheUseCase: GetMessagesCacheUseCase,
) : Actor<ChatCommand, ChatEvent>() {

    private var lastMessageId = 0L
    private var firstMessageId = 0L

    override fun execute(command: ChatCommand): Flow<ChatEvent> = flow {
        val event = when (command) {
            is ChatCommand.LoadMessage -> {
                loadMessage(command.channelName, command.topicName)
            }

            is ChatCommand.SendMessage -> {
                sendMessage(command.channelName, command.topicName, command.content)
            }

            is ChatCommand.ChangeReaction -> {
                if (command.isSelected)
                    deleteEmoji(command.messageId, command.emojiName)
                else
                    addReaction(command.messageId, command.emojiName)
            }

            is ChatCommand.ChooseReaction -> chooseReaction(command.messageId, command.emojiName)
            is ChatCommand.LoadNextMessages -> loadNextMessages(
                command.channelName,
                command.topicName
            )

            is ChatCommand.LoadPrevMessages -> loadPrevMessages(
                command.channelName,
                command.topicName
            )

            is ChatCommand.LoadMessageCache -> loadMessageCache(
                command.channelName,
                command.topicName
            )
        }
        emit(event)
    }

    private suspend fun loadMessage(channelName: String, topicName: String): ChatEvent.Domain =
        when (val result = getMessagesUseCase(channelName, topicName)) {
            is Result.Failure -> {
                ChatEvent.Domain.LoadMessagesFailure
            }

            is Result.Success -> {
                firstMessageId = result.content.first().id
                lastMessageId = result.content.last().id
                val groupMessages = result.content.groupByDate(topicName.isEmpty())
                ChatEvent.Domain.LoadMessagesSuccess(groupMessages)
            }
        }

    private suspend fun loadMessageCache(channelName: String, topicName: String): ChatEvent.Domain =
        when (val result = getMessagesCacheUseCase(channelName, topicName)) {
            is Result.Failure -> {
                ChatEvent.Domain.LoadMessagesFailure
            }

            is Result.Success -> {
                if (result.content.isEmpty()) {
                    ChatEvent.Domain.EmptyCache
                } else {
                    firstMessageId = result.content.first().id
                    lastMessageId = result.content.last().id
                    val groupMessages = result.content.groupByDate(topicName.isEmpty())
                    ChatEvent.Domain.LoadMessagesSuccess(groupMessages)
                }
            }
        }

    private suspend fun sendMessage(
        channelName: String,
        topicName: String,
        content: String
    ): ChatEvent.Domain = when (sendMessageUseCase(channelName, topicName, content)) {
        is Result.Failure -> {
            ChatEvent.Domain.SendMessageFailure
        }

        is Result.Success -> {
            ChatEvent.Domain.SendMessageSuccess
        }
    }

    private suspend fun addReaction(messageId: Long, emojiName: String): ChatEvent.Domain =
        when (addReactionUseCase(messageId, emojiName)) {
            is Result.Failure -> {
                ChatEvent.Domain.ChangeReactionFailure
            }

            is Result.Success -> {
                ChatEvent.Domain.ChangeReactionSuccess
            }
        }


    private suspend fun deleteEmoji(messageId: Long, emojiName: String): ChatEvent.Domain =
        when (removeReactionUseCase(messageId, emojiName)) {
            is Result.Failure -> {
                ChatEvent.Domain.ChangeReactionFailure
            }

            is Result.Success -> {
                ChatEvent.Domain.ChangeReactionSuccess
            }
        }

    private suspend fun chooseReaction(messageId: Long, emojiName: String): ChatEvent.Domain =
        when (val result = getMessageByIdUseCase(messageId)) {
            is Result.Failure -> ChatEvent.Domain.ChangeReactionFailure
            is Result.Success -> {
                var isSelected = false
                result.content.reactions.forEach { (key, value) ->
                    if (key.emojiName == emojiName && value.isSelected)
                        isSelected = true
                }
                if (isSelected)
                    deleteEmoji(messageId, emojiName)
                else
                    addReaction(messageId, emojiName)
            }
        }

    private suspend fun loadNextMessages(channelName: String, topicName: String): ChatEvent.Domain =
        when (val result = getNextMessagesUseCase(channelName, topicName, lastMessageId)) {
            is Result.Failure -> {
                ChatEvent.Domain.LoadMessagesFailure
            }

            is Result.Success -> {
                firstMessageId = result.content.first().id
                val isLastMessageLoaded = lastMessageId == result.content.last().id
                lastMessageId = result.content.last().id
                ChatEvent.Domain.LoadMessagesSuccess(
                    result.content.groupByDate(topicName.isEmpty()),
                    isNeedLoadNextPage = !isLastMessageLoaded,
                )
            }
        }

    private suspend fun loadPrevMessages(channelName: String, topicName: String): ChatEvent.Domain =
        when (val result = getPrevMessagesUseCase(channelName, topicName, firstMessageId)) {
            is Result.Failure -> {
                ChatEvent.Domain.LoadMessagesFailure
            }

            is Result.Success -> {
                val isFirstMessageLoaded = firstMessageId == result.content.first().id
                firstMessageId = result.content.first().id
                lastMessageId = result.content.last().id
                val groupMessages = result.content.groupByDate(topicName.isEmpty())
                ChatEvent.Domain.LoadMessagesSuccess(
                    groupMessages,
                    isNeedLoadPrevPage = !isFirstMessageLoaded,
                )
            }
        }
}