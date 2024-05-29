package com.mironov.coursework.data.network.api

import com.mironov.coursework.data.network.model.message.MessageResponse
import com.mironov.coursework.data.network.model.message.SingleMessageResponse
import com.mironov.coursework.data.network.model.presences.AllUserPresencesResponse
import com.mironov.coursework.data.network.model.presences.PresencesResponse
import com.mironov.coursework.data.network.model.streams.StreamResponse
import com.mironov.coursework.data.network.model.streams.SubscribedStreamsResponse
import com.mironov.coursework.data.network.model.topic.TopicResponse
import com.mironov.coursework.data.network.model.user.AllUsersResponse
import com.mironov.coursework.data.network.model.user.UserDto
import com.mironov.coursework.data.network.model.user.UserResponse
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ZulipApi {

    @GET("streams")
    suspend fun getAllStreams(): StreamResponse

    @GET("users/me/subscriptions")
    suspend fun getSubscribedStreams(): SubscribedStreamsResponse

    @GET("users/me/{$PATH_STREAM_ID}/topics")
    suspend fun getTopics(
        @Path(PATH_STREAM_ID) streamId: Int
    ): TopicResponse

    @GET("messages")
    suspend fun getMessages(
        @Query("anchor") anchor: String = MESSAGES_ANCHOR_FIRST_UNREAD,
        @Query("num_before") numBefore: Int = HALF_MESSAGES,
        @Query("num_after") numAfter: Int = HALF_MESSAGES,
        @Query("narrow") narrow: String,
        @Query("apply_markdown") applyMarkdown: Boolean = false,
    ): MessageResponse

    @GET("users/me")
    suspend fun getOwnProfile(): UserDto

    @GET("users")
    suspend fun getAllUsersProfile(): AllUsersResponse

    @GET("users/{$PATH_USER_ID}")
    suspend fun getUserById(
        @Path(PATH_USER_ID) id: Int
    ): UserResponse

    @POST("messages")
    suspend fun sendMessage(
        @Query("type") type: String = SEND_MESSAGE_TYPE,
        @Query("to") to: String,
        @Query("topic") topic: String,
        @Query("content") content: String
    )

    @POST("messages/{$PATH_MESSAGE_ID}/reactions")
    suspend fun addReaction(
        @Path(PATH_MESSAGE_ID) messageId: Long,
        @Query(QUERY_EMOJI_NAME) emojiName: String
    )

    @DELETE("messages/{$PATH_MESSAGE_ID}/reactions")
    suspend fun removeReaction(
        @Path(PATH_MESSAGE_ID) messageId: Long,
        @Query(QUERY_EMOJI_NAME) emojiName: String
    )

    @GET("realm/presence")
    suspend fun getAllUserStatus(): AllUserPresencesResponse

    @GET("users/{$PATH_USER_ID}/presence")
    suspend fun getUserStatusById(
        @Path(PATH_USER_ID) userId: Int
    ): PresencesResponse

    @GET("messages/{$PATH_MESSAGE_ID}")
    suspend fun getMessageById(
        @Path(PATH_MESSAGE_ID) messageId: Long,
        @Query("apply_markdown") applyMarkdown: Boolean = false,
    ): SingleMessageResponse

    @PATCH("messages/{$PATH_MESSAGE_ID}")
    suspend fun editMessageContent(
        @Path(PATH_MESSAGE_ID) messageId: Long,
        @Query("content") content: String
    )

    @PATCH("messages/{$PATH_MESSAGE_ID}")
    suspend fun editMessageTopic(
        @Path(PATH_MESSAGE_ID) messageId: Long,
        @Query("topic") topic: String
    )

    @DELETE("messages/{$PATH_MESSAGE_ID}")
    suspend fun deleteMessage(
        @Path(PATH_MESSAGE_ID) messageId: Long
    )

    @POST("users/me/subscriptions")
    suspend fun subscribedToStream(
        @Query("subscriptions") subscriptions: String
    )

    companion object {

        private const val PATH_STREAM_ID = "stream_id"
        private const val PATH_USER_ID = "user_id"
        private const val PATH_MESSAGE_ID = "message_id"

        private const val QUERY_EMOJI_NAME = "emoji_name"

        private const val MESSAGES_ANCHOR_FIRST_UNREAD = "first_unread"
        private const val SEND_MESSAGE_TYPE = "stream"

        const val MAX_MESSAGES = 20
        const val HALF_MESSAGES = 10
        const val NULL_MESSAGES = 0
    }
}