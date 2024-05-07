package com.mironov.coursework.data.network.model.streams

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SubscribedStreamsResponse(
    @SerialName("subscriptions") val streams: List<StreamDto>
)
