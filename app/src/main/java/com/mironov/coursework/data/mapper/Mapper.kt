package com.mironov.coursework.data.mapper

import com.mironov.coursework.data.database.model.message.MessageDbModel
import com.mironov.coursework.data.database.model.message.MessageInfoDbModel
import com.mironov.coursework.data.database.model.message.ReactionDbModel
import com.mironov.coursework.data.database.model.stream.StreamDbModel
import com.mironov.coursework.data.database.model.stream.TopicDbModel
import com.mironov.coursework.data.network.model.message.MessageDto
import com.mironov.coursework.data.network.model.message.ReactionDto
import com.mironov.coursework.data.network.model.presences.PresencesDto
import com.mironov.coursework.data.network.model.presences.PresencesType
import com.mironov.coursework.data.network.model.streams.StreamDto
import com.mironov.coursework.data.network.model.topic.TopicDto
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

fun MessageDbModel.toEntity(userId: Int) = Message(
    avatarUrl = message.avatarUrl,
    content = message.content,
    id = message.id,
    isMeMessage = message.senderId == userId,
    senderName = message.senderName,
    senderId = message.senderId,
    sendTime = message.timestamp.toLocalDate(),
    reactions = reactions.toEntity(userId),
)

fun List<ReactionDbModel>.toEntity(userId: Int): Map<Reaction, ReactionCondition> =
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

fun MessageDto.toDbModel(channelName: String, topicName: String, messageId: Long): MessageDbModel =
    MessageDbModel(
        message = MessageInfoDbModel(
            id = id,
            avatarUrl = avatarUrl,
            senderId = senderId,
            senderName = senderName,
            timestamp = timestamp,
            content = content,
            channelName = channelName,
            topicName = topicName
        ),
        reactions = reactions.toListDbModel(messageId)
    )

fun ReactionDto.toDbModel(messageId: Long): ReactionDbModel = ReactionDbModel(
    emojiCode = emojiCode,
    emojiName = emojiName,
    userId = userId,
    messageId = messageId
)

fun List<ReactionDto>.toListDbModel(messageId: Long): List<ReactionDbModel> =
    map { it.toDbModel(messageId) }

fun MessageDto.toEntity(userId: Int) = Message(
    avatarUrl = avatarUrl,
    content = content,
    id = id,
    isMeMessage = senderId == userId,
    senderName = senderName,
    senderId = senderId,
    sendTime = timestamp.toLocalDate(),
    reactions = reactions.toMapEntity(userId),
)

fun List<ReactionDto>.toMapEntity(userId: Int): Map<Reaction, ReactionCondition> =
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

fun PresencesDto.toEntity(): User.Presence = when {
    aggregated.status == PresencesType.ACTIVE.value
            || website.status == PresencesType.ACTIVE.value -> User.Presence.ACTIVE

    aggregated.status == PresencesType.IDLE.value && website.status == PresencesType.IDLE.value -> {
        val timesLeft = System.currentTimeMillis() / MILLIS_IN_SECONDS - aggregated.timestamp
        if (timesLeft > HOUR_IN_SECONDS) User.Presence.OFFLINE else User.Presence.IDLE
    }

    else -> User.Presence.OFFLINE
}

fun StreamDto.toDbModel(isSubscribed: Boolean): StreamDbModel = StreamDbModel(
    id = streamId,
    name = name,
    isSubscribed = isSubscribed
)

fun StreamDbModel.toEntity(): Channel = Channel(id = id, name = name)

fun TopicDbModel.toTopic(parentChannelName: String): Topic = Topic(
    id = id,
    name = name,
    messageCount = 0,
    parentChannelName = parentChannelName
)

fun TopicDto.toDbModel(streamId: Int): TopicDbModel = TopicDbModel(
    name = name,
    streamId = streamId
)

fun List<StreamDbModel>.toListChannel(): List<Channel> = map { it.toEntity() }

const val MY_ID = 708832

private const val HOUR_IN_SECONDS = 3600
private const val MILLIS_IN_SECONDS = 1000