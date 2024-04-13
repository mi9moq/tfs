package com.mironov.coursework.data.network.user

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class AllUsersResponse(
    @SerialName("members") val users: List<UserDto>
)