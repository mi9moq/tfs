package com.mironov.coursework.presentation.message

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mironov.coursework.data.Data
import com.mironov.coursework.domain.entity.Message
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.ZoneId

class MessageViewModel: ViewModel() {

    private val _messages = MutableStateFlow(Data.messages)
    val messages = _messages.asStateFlow()

    fun sentMessage(messageText: String): Boolean {
        if (messageText.isNotEmpty()){
            val date = LocalDate.now(ZoneId.systemDefault())
            val message = Message(
                content = messageText,
                id = 1,
                isMeMessage = true,
                senderId = 6,
                sendTime = date,
                senderName = "qwerty",
                reactions = emptyList()
            )

            viewModelScope.launch {
                val oldList = messages.value.toMutableList()
                oldList.add(message)
                _messages.value = oldList
            }

            return true
        } else return false
    }
}