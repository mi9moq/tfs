package com.mironov.coursework.presentation.chat

import android.content.Context

sealed interface ChatCommand {

    data class LoadMessage(
        val channelName: String,
        val topicName: String
    ) : ChatCommand

    data class SendMessage(
        val channelName: String,
        val topicName: String,
        val content: String,
    ) : ChatCommand

    data class ChangeReaction(
        val messageId: Long,
        val emojiName: String,
        val isSelected: Boolean
    ) : ChatCommand

    data class ChooseReaction(
        val messageId: Long,
        val emojiName: String,
    ) : ChatCommand

    data class LoadNextMessages(
        val channelName: String,
        val topicName: String
    ) : ChatCommand

    data class LoadPrevMessages(
        val channelName: String,
        val topicName: String
    ) : ChatCommand

    data class LoadMessageCache(
        val channelName: String,
        val topicName: String
    ) : ChatCommand

    data class LoadExistingTopics(val channelId: Int, val channelName: String) : ChatCommand

    data class ChangeTopic(val messageId: Long, val newTopic: String) : ChatCommand

    data class ChangeMessage(val messageId: Long, val newMessage: String) : ChatCommand

    data class DeleteMessage(val messageId: Long) : ChatCommand

    data class SaveMessageText(val context: Context, val text: String) : ChatCommand

    data class LoadUpdateMessageCache(
        val channelName: String,
        val topicName: String
    ) : ChatCommand
}