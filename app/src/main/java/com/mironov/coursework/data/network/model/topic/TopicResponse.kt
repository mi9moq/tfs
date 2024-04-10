package com.mironov.coursework.data.network.model.topic

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TopicResponse(
    @SerialName("topics") val topics: List<TopicResponse>
)