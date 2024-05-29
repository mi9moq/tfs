package com.mironov.coursework.domain.repository

sealed class Result<out T> {

    data class Success<T>(val content: T) : Result<T>()

    data class Failure(val message: String) : Result<Nothing>()
}