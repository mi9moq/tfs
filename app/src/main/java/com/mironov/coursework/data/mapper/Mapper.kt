package com.mironov.coursework.data.mapper

import com.mironov.coursework.data.network.model.message.MessageDto
import com.mironov.coursework.data.network.model.message.ReactionDto
import com.mironov.coursework.data.network.model.presences.PresencesDto
import com.mironov.coursework.data.network.model.streams.StreamDto
import com.mironov.coursework.data.network.model.topic.TopicDto
import com.mironov.coursework.data.network.model.topic.TopicResponse
import com.mironov.coursework.data.network.model.user.UserDto
import com.mironov.coursework.domain.entity.Channel
import com.mironov.coursework.domain.entity.Message
import com.mironov.coursework.domain.entity.Reaction
import com.mironov.coursework.domain.entity.ReactionCondition
import com.mironov.coursework.domain.entity.Topic
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

fun UserDto.toEntity(presence: User.Presence) = User(
    id = userId,
    userName = userName,
    avatarUrl = avatarUrl ?: "",
    email = email,
    presence = presence,
)

fun StreamDto.toChannel(): Channel = Channel(id = streamId, name = name)

fun List<StreamDto>.toListChannel(): List<Channel> = map { it.toChannel() }

fun TopicDto.toTopic(parentChannelName: String): Topic = Topic(
    id = maxId,
    name = name,
    messageCount = 0,
    parentChannelName = parentChannelName
)

fun TopicResponse.toListTopic(parentChannelName: String): List<Topic> = topics.map {
    it.toTopic(parentChannelName)
}

fun PresencesDto.toEntity(): User.Presence = when {
    aggregated.status == "active" || website.status == "active" -> User.Presence.ACTIVE
    aggregated.status == "idle" && website.status == "idle" -> {
        val timesLeft = System.currentTimeMillis() / 1000 - aggregated.timestamp
        if (timesLeft > 3600) User.Presence.OFFLINE else User.Presence.IDLE
    }

    else -> User.Presence.OFFLINE
}

const val MY_ID = 708832