package com.mironov.coursework.di.app.module

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.mironov.coursework.data.network.AuthInterceptor
import com.mironov.coursework.data.network.api.ZulipApi
import com.mironov.coursework.di.app.annotation.AppScope
import com.mironov.coursework.di.app.annotation.BaseUrl
import dagger.Module
import dagger.Provides
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.create

@Module
object NetworkModule {

    private const val MEDIA_TYPE = "application/json"

    @AppScope
    @Provides
    fun provideJsonSerializer(): Json = Json {
        ignoreUnknownKeys = true
    }

    @AppScope
    @Provides
    fun provideOkHttpClient(authInterceptor: AuthInterceptor): OkHttpClient = OkHttpClient
        .Builder()
        .addInterceptor(authInterceptor)
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        .build()

    @AppScope
    @Provides
    fun provideRetrofit(
        client: OkHttpClient,
        jsonSerializer: Json,
        @BaseUrl baseUrl: String
    ): Retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(jsonSerializer.asConverterFactory(MEDIA_TYPE.toMediaType()))
        .client(client)
        .build()

    @AppScope
    @Provides
    fun provideZulipApi(retrofit: Retrofit): ZulipApi = retrofit.create()
}