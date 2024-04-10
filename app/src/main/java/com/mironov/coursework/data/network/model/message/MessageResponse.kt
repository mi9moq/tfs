package com.mironov.coursework.data.network.model.message

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MessageResponse(
    @SerialName("messages") val messages: List<MessageDto>
)