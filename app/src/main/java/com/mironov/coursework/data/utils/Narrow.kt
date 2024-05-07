package com.mironov.coursework.data.utils

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Narrow(
    @SerialName("operator") val operator: String,
    @SerialName("operand") val operand: String,
) {

    companion object {
        const val STREAM = "stream"
        const val TOPIC = "topic"
    }
}