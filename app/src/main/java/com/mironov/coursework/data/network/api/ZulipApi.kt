package com.mironov.coursework.data.network.api

import com.mironov.coursework.data.network.model.streams.StreamResponse
import com.mironov.coursework.data.network.model.streams.SubscribedStreamsResponse
import retrofit2.http.GET

interface ZulipApi {

    @GET("streams")
    suspend fun getAllStreams(): StreamResponse

    @GET("users/me/subscriptions")
    suspend fun getSubscribedStreams(): SubscribedStreamsResponse
}