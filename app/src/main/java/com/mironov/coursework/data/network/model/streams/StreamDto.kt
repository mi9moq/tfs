package com.mironov.coursework.data.network.model.streams

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StreamDto(
    @SerialName("stream_id") val streamId: Int,
    @SerialName("name") val name: String,
)