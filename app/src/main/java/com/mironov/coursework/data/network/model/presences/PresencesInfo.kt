package com.mironov.coursework.data.network.model.presences

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PresencesInfo(
    @SerialName("status") val status: String,
    @SerialName("timestamp") val timestamp: Long
)