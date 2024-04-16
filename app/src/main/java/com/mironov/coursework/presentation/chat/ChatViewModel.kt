package com.mironov.coursework.presentation.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mironov.coursework.data.mapper.MY_ID
import com.mironov.coursework.data.mapper.toListEntity
import com.mironov.coursework.data.network.api.ZulipApi
import com.mironov.coursework.navigation.router.ChatRouter
import com.mironov.coursework.ui.utils.groupByDate
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject

class ChatViewModel @Inject constructor(
    private val router: ChatRouter,
    private val api: ZulipApi
) : ViewModel() {

    private val _state = MutableStateFlow<ChatState>(ChatState.Initial)
    val state = _state.asStateFlow()

    fun loadMessages(channelName: String, topicName: String) {
        viewModelScope.launch {
            val narrow = mutableListOf<Narrow>()
            narrow.add(Narrow(Narrow.STREAM, channelName))
            narrow.add(Narrow(Narrow.TOPIC, topicName))

            val messages = api
                .getMessages(narrow = Json.encodeToString(narrow))
                .messages.toListEntity(MY_ID)
            _state.value = ChatState.Content(messages.groupByDate())
        }
    }

    fun sendMessage(channelName: String, topicName: String, messageText: String) {
        viewModelScope.launch {
            api.sendMessage(to = channelName, topic = topicName, content = messageText)
        }
    }

    fun changeReaction(messageId: Long, emojiName: String, isSelected: Boolean) {
        if (isSelected) {
            removeReaction(messageId, emojiName)
        } else {
            addReaction(messageId, emojiName)
        }
    }

    fun addReaction(messageId: Long, emojiName: String) {
        viewModelScope.launch {
            api.addReaction(messageId, emojiName)
        }
    }

    private fun removeReaction(messageId: Long, emojiName: String) {
        viewModelScope.launch {
            api.removeReaction(messageId, emojiName)
        }
    }

    fun back() {
        router.back()
    }
}