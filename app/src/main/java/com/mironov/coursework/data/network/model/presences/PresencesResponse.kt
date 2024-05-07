package com.mironov.coursework.data.network.model.presences

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PresencesResponse(
    @SerialName("presence") val presences: PresencesDto
)