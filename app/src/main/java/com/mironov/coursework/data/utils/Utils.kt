package com.mironov.coursework.data.utils

import com.mironov.coursework.domain.repository.Result
import kotlinx.coroutines.CancellationException

suspend fun <T> runCatchingNonCancellation(
    block: suspend () -> Result<T>
): Result<T> {
    return try {
        block()
    } catch (e: CancellationException) {
        throw e
    } catch (e: Exception) {
        Result.Failure(e.message ?: "")
    }
}