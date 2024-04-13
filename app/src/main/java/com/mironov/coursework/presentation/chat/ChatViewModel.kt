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
import javax.inject.Inject

class ChatViewModel @Inject constructor(
    private val router: ChatRouter,
    private val api: ZulipApi
) : ViewModel() {

    private val _state = MutableStateFlow<ChatState>(ChatState.Initial)
    val state = _state.asStateFlow()

    fun loadMessages(chatId: Int) {
        viewModelScope.launch {
            val messages = api.getMessages().messages.toListEntity(708832)
            _state.value = ChatState.Content(messages.groupByDate())
        }
    }

    fun sendMessage(messageText: String) {
        viewModelScope.launch {
            api.sendMessage(to = "general", topic = "testing", content = messageText)
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