package com.mironov.coursework.presentation.message

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mironov.coursework.data.Data
import com.mironov.coursework.domain.entity.Message
import com.mironov.coursework.domain.entity.Reaction
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.ZoneId

class MessageViewModel : ViewModel() {

    private val _messages = MutableStateFlow(Data.messages)
    val messages
        get() = _messages.asStateFlow()

    fun sendMessage(messageText: String, id: Int): Boolean {
        if (messageText.isNotEmpty()) {
            val date = LocalDate.now(ZoneId.systemDefault())
            val message = Message(
                content = messageText,
                id = id,
                isMeMessage = true,
                senderId = 6,
                sendTime = date,
                senderName = "qwerty",
                reactions = mutableSetOf()
            )

            viewModelScope.launch {
                val oldList = messages.value.toMutableList()
                oldList.add(message)
                _messages.value = oldList.toList()
            }

            return true
        } else return false
    }

    fun addReaction(messageId: Int, emojiUnicode: Int) {
        viewModelScope.launch {
            val oldMessages = messages.value
            val ind = oldMessages.indexOfFirst {
                it.id == messageId
            }
            if (ind != -1) {
                oldMessages[ind].reactions.forEach { reaction ->
                    if (reaction.emojiUnicode == emojiUnicode) {
                        return@launch
                    }
                }
                val newReaction = Reaction(
                    emojiUnicode = emojiUnicode,
                    count = 1,
                    isSelected = true
                )
                oldMessages[ind].reactions.add(newReaction)
                _messages.value = oldMessages.toList()
            }

        }
    }

    fun changeReaction(messageId: Int, emojiUnicode: Int) {
        viewModelScope.launch {
            val oldMessages = messages.value
            val ind = oldMessages.indexOfFirst {
                it.id == messageId
            }

            if (ind != -1) {
                val r = oldMessages[ind].reactions.find {
                    it.emojiUnicode == emojiUnicode
                } ?: return@launch

                val count = r.count

                if (r.isSelected && count == 1) {
                    oldMessages[ind].reactions.remove(r)
                    return@launch
                }

                val newReaction = if (r.isSelected) {
                    r.copy(isSelected = false, count = count - 1)
                } else {
                    r.copy(isSelected = true, count = count + 1)
                }
                oldMessages[ind].reactions.remove(r)
                oldMessages[ind].reactions.add(newReaction)

                _messages.value = oldMessages
            }
        }
    }
}