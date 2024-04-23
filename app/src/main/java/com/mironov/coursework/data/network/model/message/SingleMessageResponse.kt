package com.mironov.coursework.data.network.model.message

import kotlinx.serialization.Serializable

@Serializable
data class SingleMessageResponse(
    val message: MessageDto
)