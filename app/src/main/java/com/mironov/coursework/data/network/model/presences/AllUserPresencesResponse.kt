package com.mironov.coursework.data.network.model.presences

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AllUserPresencesResponse(
    @SerialName("presences") val presences: Map<String, PresencesDto>,
)