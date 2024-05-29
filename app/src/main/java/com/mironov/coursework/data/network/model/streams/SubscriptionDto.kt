package com.mironov.coursework.data.network.model.streams

import kotlinx.serialization.Serializable

@Serializable
data class SubscriptionDto(
    val name: String,
    val description: String
)
