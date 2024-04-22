package com.mironov.coursework.presentation.chat

import com.mironov.coursework.domain.repository.Result
import com.mironov.coursework.domain.usecase.AddReactionUseCase
import com.mironov.coursework.domain.usecase.GetMessagesUseCase
import com.mironov.coursework.domain.usecase.RemoveReactionUseCase
import com.mironov.coursework.domain.usecase.SendMessageUseCase
import com.mironov.coursework.ui.adapter.DelegateItem
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
) : Actor<ChatCommand, ChatEvent>() {

    private val cache = mutableListOf<DelegateItem>()

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
        }
        emit(event)
    }

    private suspend fun loadMessage(channelName: String, topicName: String): ChatEvent.Domain =
        when (val result = getMessagesUseCase(channelName, topicName)) {
            is Result.Failure -> {
                ChatEvent.Domain.LoadMessagesFailure
            }

            is Result.Success -> {
                cache.clear()
                val groupMessages = result.content.groupByDate()
                cache.addAll(groupMessages)
                ChatEvent.Domain.LoadMessagesSuccess(groupMessages)
            }
        }

    private suspend fun sendMessage(
        channelName: String,
        topicName: String,
        content: String
    ): ChatEvent.Domain = when (sendMessageUseCase(channelName, topicName, content)) {
        is Result.Failure -> {
            ChatEvent.Domain.SendMessageFailure(cache)
        }

        is Result.Success -> {
            ChatEvent.Domain.SendMessageSuccess(cache)
        }
    }

    private suspend fun addReaction(messageId: Long, emojiName: String): ChatEvent.Domain =
        when (addReactionUseCase(messageId, emojiName)) {
            is Result.Failure -> {
                ChatEvent.Domain.ChangeReactionFailure(cache)
            }

            is Result.Success -> {
                ChatEvent.Domain.ChangeReactionSuccess(cache)
            }
        }


    private suspend fun deleteEmoji(messageId: Long, emojiName: String): ChatEvent.Domain =
        when (removeReactionUseCase(messageId, emojiName)) {
            is Result.Failure -> {
                ChatEvent.Domain.ChangeReactionFailure(cache)
            }

            is Result.Success -> {
                ChatEvent.Domain.ChangeReactionSuccess(cache)
            }
        }
}