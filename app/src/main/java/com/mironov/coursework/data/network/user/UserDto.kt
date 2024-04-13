package com.mironov.coursework.data.network.user

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserDto(
    @SerialName("avatar_url") val avatarUrl: String?,
    @SerialName("full_name") val userName: String,
    @SerialName("email") val email: String,
    @SerialName("user_id") val userId: Int
)