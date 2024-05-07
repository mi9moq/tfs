package com.mironov.coursework.data.repository

import com.mironov.coursework.data.network.api.ZulipApi
import com.mironov.coursework.data.utils.runCatchingNonCancellation
import com.mironov.coursework.di.app.annotation.IoDispatcher
import com.mironov.coursework.domain.repository.ReactionRepository
import com.mironov.coursework.domain.repository.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ReactionRepositoryImpl @Inject constructor(
    private val api: ZulipApi,
    @IoDispatcher private val dispatcher: CoroutineDispatcher,
) : ReactionRepository {

    override suspend fun addReaction(
        messageId: Long,
        emojiName: String
    ): Result<Boolean> = withContext(dispatcher) {
        runCatchingNonCancellation {
            api.addReaction(messageId, emojiName)
            Result.Success(true)
        }
    }

    override suspend fun removeReaction(
        messageId: Long,
        emojiName: String
    ): Result<Boolean> = withContext(dispatcher) {
        runCatchingNonCancellation {
            api.removeReaction(messageId, emojiName)
            Result.Success(true)
        }
    }
}