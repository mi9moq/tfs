package com.mironov.coursework.di

import com.mironov.coursework.di.app.annotation.AppScope
import com.mironov.coursework.di.app.annotation.BaseUrl
import dagger.Module
import dagger.Provides

@Module
class TestBaseUrlModule {

    companion object {
        private const val TEST_BASE_URL = "http://localhost:8080"
    }

    @AppScope
    @BaseUrl
    @Provides
    fun providesTestBaseUrl(): String = TEST_BASE_URL
}