package com.mironov.coursework.data.network.api

import com.mironov.coursework.data.network.model.message.MessageResponse
import com.mironov.coursework.data.network.model.presences.AllUserPresencesResponse
import com.mironov.coursework.data.network.model.presences.PresencesResponse
import com.mironov.coursework.data.network.model.streams.StreamResponse
import com.mironov.coursework.data.network.model.streams.SubscribedStreamsResponse
import com.mironov.coursework.data.network.model.topic.TopicResponse
import com.mironov.coursework.data.network.model.user.AllUsersResponse
import com.mironov.coursework.data.network.model.user.UserDto
import com.mironov.coursework.data.network.model.user.UserResponse
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
        @Path("stream_id") streamId: Int
    ): TopicResponse

    @GET("messages")
    suspend fun getMessages(
        @Query("anchor") anchor: String = "newest",
        @Query("num_before") numBefore: Int = 15,
        @Query("num_after") numAfter: Int = 0,
        @Query("narrow") narrow: String,
        @Query("apply_markdown")  applyMarkdown: Boolean = false,
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

    @GET("realm/presence")
    suspend fun getAllUserStatus(): AllUserPresencesResponse

    @GET("users/{user_id}/presence")
    suspend fun getUserStatusById(
        @Path("user_id") userId: Int
    ): PresencesResponse

    @POST("users/me/presence?status=active")
    suspend fun setOwnStatusActive()
}