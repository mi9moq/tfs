package com.mironov.coursework.presentation.chat

sealed interface ChatCommand {

    data class LoadMessage(
        val channelName: String,
        val topicName: String
    ) : ChatCommand

    data class SendMessage (
        val channelName: String,
        val topicName: String,
        val content: String,
    ) : ChatCommand

    data class ChangeReaction(
        val messageId: Long,
        val emojiName: String,
        val isSelected: Boolean
    ) : ChatCommand
}