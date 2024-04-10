package com.mironov.coursework.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.mironov.coursework.data.network.AuthInterceptor
import com.mironov.coursework.data.network.api.ZulipApi
import dagger.Module
import dagger.Provides
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.create

@Module
object NetworkModule {

    private const val BASE_URL = "https://tinkoff-android-spring-2024.zulipchat.com/api/v1/"
    private const val MEDIA_TYPE = "application/json"

    @AppScope
    @Provides
    fun provideJsonSerializer(): Json = Json {
        ignoreUnknownKeys = true
    }

    @AppScope
    @Provides
    fun provideMediaType(): MediaType = MediaType.get(MEDIA_TYPE)

    @AppScope
    @Provides
    fun provideOkHttpClient(authInterceptor: AuthInterceptor): OkHttpClient = OkHttpClient
        .Builder()
        .addInterceptor(authInterceptor)
        .build()

    @AppScope
    @Provides
    fun provideRetrofit(
        client: OkHttpClient,
        jsonSerializer: Json,
        mediaType: MediaType
    ): Retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(jsonSerializer.asConverterFactory(mediaType))
            .client(client)
            .build()

    @AppScope
    @Provides
    fun provideZulipApi(retrofit: Retrofit): ZulipApi = retrofit.create()
}