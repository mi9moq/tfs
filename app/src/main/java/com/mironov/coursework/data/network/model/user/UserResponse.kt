package com.mironov.coursework.data.network.model.user

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class UserResponse(
    @SerialName("user") val user: UserDto
)