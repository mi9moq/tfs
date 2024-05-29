package com.mironov.coursework.di.app.module

import com.mironov.coursework.di.app.annotation.AppScope
import com.mironov.coursework.di.app.annotation.BaseUrl
import dagger.Module
import dagger.Provides

@Module
class BaseUrlModule {

    companion object {
        private const val BASE_URL = "https://tinkoff-android-spring-2024.zulipchat.com/api/v1/"
    }

    @AppScope
    @BaseUrl
    @Provides
    fun provideBaseUrl(): String = BASE_URL
}