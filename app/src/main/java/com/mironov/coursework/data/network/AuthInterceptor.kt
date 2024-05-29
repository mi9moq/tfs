package com.mironov.coursework.data.network

import okhttp3.Credentials
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor() : Interceptor {

    companion object {
        private const val PARAM_AUTHORIZATION = "Authorization"
        private const val USERNAME = "mironovdeniswork@gmail.com"
        private const val API_KEY = "8DKeAw1Yk41GQmsrVoQevr1LJjaJ943B"
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder().addHeader(
            PARAM_AUTHORIZATION,
            Credentials.basic(USERNAME, API_KEY)
        ).build()
        return chain.proceed(requestBuilder)
    }
}