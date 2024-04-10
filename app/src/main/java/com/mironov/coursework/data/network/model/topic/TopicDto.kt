package com.mironov.coursework.data.network.model.topic

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TopicDto(
    @SerialName("max_id") val maxId: Long,
    @SerialName("name") val name: String,
)