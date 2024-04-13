package com.mironov.coursework.data.network.api

import com.mironov.coursework.data.network.model.message.MessageResponse
import com.mironov.coursework.data.network.model.streams.StreamResponse
import com.mironov.coursework.data.network.model.streams.SubscribedStreamsResponse
import com.mironov.coursework.data.network.user.AllUsersResponse
import com.mironov.coursework.data.network.user.UserDto
import com.mironov.coursework.data.network.user.UserResponse
import retrofit2.http.GET
import retrofit2.http.POST
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
        @Query("num_before") numBefore: Int = 15,
        @Query("num_after") numAfter: Int = 0,
    ): MessageResponse

    @GET("users/me")
    suspend fun getMyProfile(): UserDto

    @GET("users")
    suspend fun getAllUsersProfile(): AllUsersResponse

    @GET("users/{user_id}")
    suspend fun getUserById(
        @Path("user_id") id: Int
    ): UserResponse

    @POST("messages")
    suspend fun sendMessage(
        @Query("type") type: String = "stream",
        @Query("to") to: String,
        @Query("topic") topic: String,
        @Query("content") content: String
    )

    @POST("messages/{message_id}/reactions")
    suspend fun addReaction(
        @Path("message_id") messageId: Long,
        @Query("emoji_name") emojiName: String
    )

    @POST("messages/{message_id}/reactions")
    suspend fun removeReaction(
        @Path("message_id") messageId: Long,
        @Query("emoji_name") emojiName: String
    )
}