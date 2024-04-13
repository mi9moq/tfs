package com.mironov.coursework.data.network.api

import com.mironov.coursework.data.network.model.message.MessageResponse
import com.mironov.coursework.data.network.model.streams.StreamResponse
import com.mironov.coursework.data.network.model.streams.SubscribedStreamsResponse
import com.mironov.coursework.data.network.user.AllUsersResponse
import com.mironov.coursework.data.network.user.UserDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ZulipApi {

    @GET("streams")
    suspend fun getAllStreams(): StreamResponse

    @GET("users/me/subscriptions")
    suspend fun getSubscribedStreams(): SubscribedStreamsResponse

    @GET("users/me/{stream_id}/topics")
    suspend fun getTopics(
        @Path("stream_id") streamId: Long
    )

    @GET("messages")
    suspend fun getMessages(
        @Query("anchor") anchor: String = "newest",
        @Query("num_before") numBefore: Int = 150,
        @Query("num_after") numAfter: Int = 0,
    ): MessageResponse

    @GET("users/me")
    suspend fun getMyProfile(): UserDto

    @GET("users")
    suspend fun getAllUsersProfile(): AllUsersResponse
}