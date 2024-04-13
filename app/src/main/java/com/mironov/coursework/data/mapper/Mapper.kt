package com.mironov.coursework.data.mapper

import com.mironov.coursework.data.network.model.message.MessageDto
import com.mironov.coursework.data.network.model.message.ReactionDto
import com.mironov.coursework.data.network.user.UserDto
import com.mironov.coursework.domain.entity.Message
import com.mironov.coursework.domain.entity.Reaction
import com.mironov.coursework.domain.entity.ReactionCondition
import com.mironov.coursework.domain.entity.User
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

fun List<MessageDto>.toListEntity(userId: Int): List<Message> = map {
    it.toEntity(userId)
}

val regex = Regex("<[^>]*>")

fun MessageDto.toEntity(userId: Int) = Message(
    avatarUrl = avatarUrl,
    content = content.replace(regex, ""),
    id = id,
    isMeMessage = senderId == userId,
    senderName = senderName,
    senderId = senderId,
    sendTime = timestamp.toLocalDate(),
    reactions = reactions.toEntity(userId),
)

fun List<ReactionDto>.toEntity(userId: Int): Map<Reaction, ReactionCondition> =
    associate { reactionDto ->
        Reaction(
            emojiUnicode = reactionDto.emojiCode.toIntOrNull(16) ?: 0x1F916,
            emojiName = reactionDto.emojiName
        ) to ReactionCondition(
            isSelected = reactionDto.userId == userId,
            count = this.count {
                it.emojiCode == reactionDto.emojiCode
            }
        )
    }

fun Long.toLocalDate(): LocalDate {
    val zoneId = ZoneId.systemDefault()
    return Instant.ofEpochSecond(this).atZone(zoneId).toLocalDate()
}

fun UserDto.toEntity() = User(
    id = userId,
    userName = userName,
    avatarUrl = avatarUrl ?: "",
    email = email,
    isOnline = false,
    status = ""
)

fun List<UserDto>.toListEntity(): List<User> = map { it.toEntity() }

const val MY_ID = 708832