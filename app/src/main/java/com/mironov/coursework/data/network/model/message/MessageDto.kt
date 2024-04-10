package com.mironov.coursework.data.network.model.message

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MessageDto(
    @SerialName("avatar_url") val avatarUrl: String?,
    @SerialName("id") val id: Long,
    @SerialName("sender_id") val senderId: Long,
    @SerialName("sender_full_name") val senderName: String,
    @SerialName("timestamp") val timestamp: Long,
    @SerialName("content") val content: String,
    @SerialName("is_me_message") val isMeMessage: Boolean,
    @SerialName("reactions") val reactions: List<ReactionDto>,
)