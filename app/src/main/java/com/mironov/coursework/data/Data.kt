package com.mironov.coursework.data

import com.mironov.coursework.domain.entity.Message
import com.mironov.coursework.domain.entity.Reaction
import java.time.LocalDate
import java.time.Month
import java.time.ZoneId

object Data {

    private val date1 = LocalDate.now(ZoneId.systemDefault())
    private val date2 = LocalDate.of(2024, Month.MARCH, 22)
    private val date3 = LocalDate.of(2024, Month.MARCH, 21)
    private val reactionSet1 = mutableSetOf(
        Reaction(
            0x1f44d,
            2,
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
    )

    private val reactionSet2 = mutableSetOf(
        reactionList[3].copy(count = 3),
        reactionList[4].copy(count = 1),
        reactionList[1].copy(count = 9),
        reactionList[8].copy(count = 33),
    )

    val messages = listOf(
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
            reactions = reactionSet1,
        ),
        Message(
            avatarUrl = null,
            content = "Message 1",
            id = 3,
            isMeMessage = false,
            senderName = "Sender name 1",
            senderId = 1,
            sendTime = date2,
            reactions = reactionSet2,
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
            reactions = reactionList.take(8).toMutableSet()
        )
    )

    private val reactionSet3 = mutableSetOf(
        reactionList[3].copy(count = 4, isSelected = true),
        reactionList[4].copy(count = 1),
        reactionList[1].copy(count = 12, isSelected = true),
        reactionList[8].copy(count = 23),
    )

    val messages2 = listOf(
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
            reactions = reactionSet2,
        ),
        Message(
            avatarUrl = null,
            content = "Message 1",
            id = 3,
            isMeMessage = false,
            senderName = "Sender name 1",
            senderId = 1,
            sendTime = date2,
            reactions = reactionSet3,
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
            reactions = reactionList.take(8).toMutableSet()
        )
    )
}