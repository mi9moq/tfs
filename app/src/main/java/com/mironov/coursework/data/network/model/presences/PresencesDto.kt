package com.mironov.coursework.data.network.model.presences

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PresencesDto(
    @SerialName("aggregated") val aggregated: PresencesInfo,
    @SerialName("website") val website: PresencesInfo,
)