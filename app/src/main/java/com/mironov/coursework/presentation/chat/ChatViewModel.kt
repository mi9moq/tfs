package com.mironov.coursework.presentation.chat

import androidx.lifecycle.ViewModel
import com.mironov.coursework.data.reactionList
import com.mironov.coursework.domain.entity.Message
import com.mironov.coursework.domain.entity.Reaction
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.LocalDate
import java.time.Month
import java.time.ZoneId

class ChatViewModel : ViewModel() {

    private val _messages = MutableStateFlow<ChatState>(ChatState.Content(emptyList()))
    val messages = _messages.asStateFlow()

    private val date1 = LocalDate.now(ZoneId.systemDefault())
    private val date2 = LocalDate.of(2024, Month.MARCH, 22)
    private val date3 = LocalDate.of(2024, Month.MARCH, 21)

    private val data = mutableListOf(
        Message(
            avatarUrl = null,
            content = "Message 1",
            id = 1,
            isMeMessage = false,
            senderName = "Sender name 1",
            senderId = 1,
            sendTime = date3,
            reactions = mutableSetOf(),
        ),
        Message(
            avatarUrl = null,
            content = "Message 2 Message 2 Message 2 Message 2 Message 2 Message 2 Message 2 Message 2 Message 2 Message 2 Message 2 Message 2 Message 2 Message 2",
            id = 2,
            isMeMessage = true,
            senderName = "Sender name 2",
            senderId = 1,
            sendTime = date2,
            reactions = mutableSetOf(
                Reaction(
                    0x1f44d,
                    1,
                    1,
                    true
                ),
                Reaction(
                    0x1f600,
                    3,
                    1
                ),
                Reaction(
                    0x1f921,
                    5,
                    1
                )
            ),
        ),
        Message(
            avatarUrl = null,
            content = "Message 1",
            id = 3,
            isMeMessage = false,
            senderName = "Sender name 1",
            senderId = 1,
            sendTime = date2,
            reactions = mutableSetOf(
                reactionList[3].copy(count = 3),
                reactionList[4].copy(count = 1, isSelected = true),
                reactionList[1].copy(count = 9),
                reactionList[8].copy(count = 33),
            ),
        ),
        Message(
            avatarUrl = null,
            content = "Message 3 \n" +
                    " Тут мог быть осмысленный текст, но пока обойдусь без него",
            id = 4,
            isMeMessage = false,
            senderName = "Sender name 1",
            senderId = 1,
            sendTime = date1,
            reactions = mutableSetOf()
        ),
        Message(
            avatarUrl = null,
            content = "Message Message Mes",
            id = 5,
            isMeMessage = true,
            senderName = "Sender name 2",
            senderId = 1,
            sendTime = date2,
            reactions = mutableSetOf(
                reactionList[1].copy(count = 9),
                reactionList[8].copy(count = 33),
            ),
        ),
    )

    fun loadMessages(chatId: Int) {
        _messages.value = ChatState.Content(data.toList())
    }

    fun sendMessage(messageText: String, id: Int): Boolean {
        return if (messageText.isNotEmpty()) {
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
            data.add(message)
            _messages.value = ChatState.Content(data.toList())
            true
        } else false
    }

    fun addReaction(messageId: Int, emojiUnicode: Int) {

        _messages.value = ChatState.Loading

        val ind = data.indexOfFirst {
            it.id == messageId
        }
        if (ind != -1) {
            data[ind].reactions.forEach { reaction ->
                if (reaction.emojiUnicode == emojiUnicode) {
                    _messages.value = ChatState.Content(data.toList())
                    return
                }
            }
            val newReaction = Reaction(
                emojiUnicode = emojiUnicode,
                count = 1,
                isSelected = true
            )
            data[ind].reactions.add(newReaction)
            _messages.value = ChatState.Content(data.toList())
        }
    }

    fun changeReaction(messageId: Int, emojiUnicode: Int) {
        _messages.value = ChatState.Loading
        val ind = data.indexOfFirst {
            it.id == messageId
        }

        if (ind != -1) {
            val r = data[ind].reactions.find {
                it.emojiUnicode == emojiUnicode
            } ?: return

            val count = r.count

            if (r.isSelected && count == 1) {
                data[ind].reactions.remove(r)
                _messages.value = ChatState.Content(data.toList())
                return
            }

            val newReaction = if (r.isSelected) {
                r.copy(isSelected = false, count = count - 1)
            } else {
                r.copy(isSelected = true, count = count + 1)
            }
            data[ind].reactions.remove(r)
            data[ind].reactions.add(newReaction)

            _messages.value = ChatState.Content(data.toList())
        }
    }
}