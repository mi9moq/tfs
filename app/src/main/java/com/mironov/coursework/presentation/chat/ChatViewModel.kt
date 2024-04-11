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
            val messages = api.getMessages().messages.toListEntity(1L)
            _state.value = ChatState.Content(messages.groupByDate())
        }
    }

    fun sendMessage(messageText: String) {
        viewModelScope.launch {

        }
    }

    fun addReaction(messageId: Long, emojiUnicode: Int) {
        viewModelScope.launch {
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