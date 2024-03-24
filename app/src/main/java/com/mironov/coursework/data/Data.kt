package com.mironov.coursework.data

import com.mironov.coursework.domain.entity.Message
import com.mironov.coursework.domain.entity.Reaction
import java.time.LocalDate
import java.time.Month
import java.time.ZoneId

object Data {

    private val date1 = LocalDate.now(ZoneId.systemDefault())
    private val date2 = LocalDate.of(2024, Month.MARCH,22)
    private val date3 = LocalDate.of(2024, Month.MARCH,21)
    private val reactions = listOf(
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

    val messages = listOf(
        Message(
            avatarUrl = null,
            content = "Message 1",
            id = 1,
            isMeMessage = false,
            senderName = "Sender name 1",
            senderId = 1,
            sendTime = date3,
            reactions = emptyList(),
        ),
        Message(
            avatarUrl = null,
            content = "Message 2 Message 2 Message 2 Message 2 Message 2 Message 2 Message 2 Message 2 Message 2 Message 2 Message 2 Message 2 Message 2 Message 2",
            id = 2,
            isMeMessage = true,
            senderName = "Sender name 2",
            senderId = 1,
            sendTime = date2,
            reactions = reactions,
        ),
        Message(
            avatarUrl = null,
            content = "Message 1",
            id = 3,
            isMeMessage = false,
            senderName = "Sender name 1",
            senderId = 1,
            sendTime = date2,
            reactions = reactions,
        ),
        Message(
            avatarUrl = null,
            content = "Message 3 \n" +
                    " Насрал целую кучу текста уцйуц уцй уцй уцйуу ааааа укуцй у ывф",
            id = 4,
            isMeMessage = false,
            senderName = "Sender name 1",
            senderId = 1,
            sendTime = date1,
            reactions = reactions + listOf(Reaction(
                0x1f45d,
                1,
                1,
                true
            )),
        )
    )
}