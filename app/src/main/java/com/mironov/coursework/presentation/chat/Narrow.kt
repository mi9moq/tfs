package com.mironov.coursework.presentation.chat

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Narrow(
    @SerialName("operator") val operator: String,
    @SerialName("operand") val operand: String,
)