package com.mironov.coursework.presentation.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mironov.coursework.data.mapper.MY_ID
import com.mironov.coursework.data.mapper.toListEntity
import com.mironov.coursework.data.network.api.ZulipApi
import com.mironov.coursework.navigation.router.ChatRouter
import com.mironov.coursework.ui.adapter.DelegateItem
import com.mironov.coursework.ui.utils.groupByDate
import kotlinx.coroutines.CancellationException
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

    private val cache = mutableListOf<DelegateItem>()

    fun loadMessages(channelName: String, topicName: String) {
        viewModelScope.launch {
            val narrow = mutableListOf<Narrow>()
            narrow.add(Narrow(Narrow.STREAM, channelName))
            narrow.add(Narrow(Narrow.TOPIC, topicName))
            cache.clear()

            try {
                val messages = api
                    .getMessages(narrow = Json.encodeToString(narrow))
                    .messages.toListEntity(MY_ID).groupByDate()
                cache.addAll(messages)
                _state.value = ChatState.Content(messages)
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _state.value = ChatState.Error.LoadingError
            }
        }
    }

    fun sendMessage(channelName: String, topicName: String, messageText: String) {
        viewModelScope.launch {
            try {
                api.sendMessage(to = channelName, topic = topicName, content = messageText)
                val narrow = mutableListOf<Narrow>()
                narrow.add(Narrow(Narrow.STREAM, channelName))
                narrow.add(Narrow(Narrow.TOPIC, topicName))
                val messages = api
                    .getMessages(narrow = Json.encodeToString(narrow))
                    .messages.toListEntity(MY_ID).groupByDate()
                cache.addAll(messages)
                _state.value = ChatState.Content(messages)
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _state.value = ChatState.Error.SendingError(cache)
            }
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
            try {
                api.addReaction(messageId, emojiName)
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _state.value = ChatState.Error.ChangeRationError(cache)
            }
        }
    }

    private fun removeReaction(messageId: Long, emojiName: String) {
        viewModelScope.launch {
            try {
                api.removeReaction(messageId, emojiName)
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _state.value = ChatState.Error.ChangeRationError(cache)
            }
        }
    }

    fun back() {
        router.back()
    }
}