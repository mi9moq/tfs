package com.mironov.coursework.presentation.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
            narrow.add(Narrow("stream", channelName))
            narrow.add(Narrow("topic", topicName))

            val messages =
                api.getMessages(narrow = Json.encodeToString(narrow)).messages.toListEntity(708832)
            _state.value = ChatState.Content(messages.groupByDate())
        }
    }

    fun sendMessage(channelName: String, topicName: String, messageText: String) {
        viewModelScope.launch {
            api.sendMessage(to = channelName, topic = topicName, content = messageText)
        }
    }

    fun addReaction(messageId: Long, emojiName: String) {
        viewModelScope.launch {
            api.addReaction(messageId, emojiName)
        }
    }

    fun changeReaction(messageId: Long, emojiUnicode: Int) {
        viewModelScope.launch {
        }
    }

    fun back() {
        router.back()
    }
}