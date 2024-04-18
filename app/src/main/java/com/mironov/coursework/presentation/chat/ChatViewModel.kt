package com.mironov.coursework.presentation.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mironov.coursework.data.network.api.ZulipApi
import com.mironov.coursework.domain.repository.Result
import com.mironov.coursework.domain.usecase.GetMessagesUseCase
import com.mironov.coursework.domain.usecase.SendMessageUseCase
import com.mironov.coursework.navigation.router.ChatRouter
import com.mironov.coursework.ui.adapter.DelegateItem
import com.mironov.coursework.ui.utils.groupByDate
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class ChatViewModel @Inject constructor(
    private val router: ChatRouter,
    private val sendMessageUseCase: SendMessageUseCase,
    private val getMessagesUseCase: GetMessagesUseCase,
    private val api: ZulipApi
) : ViewModel() {

    private val _state = MutableStateFlow<ChatState>(ChatState.Initial)
    val state = _state.asStateFlow()

    private val cache = mutableListOf<DelegateItem>()

    fun loadMessages(channelName: String, topicName: String) {
        viewModelScope.launch {
            when (val result = getMessagesUseCase(channelName, topicName)) {
                is Result.Success -> {
                    cache.clear()
                    cache.addAll(result.content.groupByDate())
                    _state.value = ChatState.Content(result.content.groupByDate())
                }

                is Result.Failure -> _state.value = ChatState.Error.LoadingError
            }
        }
    }

    fun sendMessage(channelName: String, topicName: String, messageText: String) {
        viewModelScope.launch {
            when (sendMessageUseCase(channelName, topicName, messageText)) {
                is Result.Success -> Unit
                is Result.Failure -> _state.value = ChatState.Error.LoadingError
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