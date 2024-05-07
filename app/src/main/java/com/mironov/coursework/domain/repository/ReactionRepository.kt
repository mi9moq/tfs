package com.mironov.coursework.domain.repository

interface ReactionRepository {

    suspend fun addReaction(messageId: Long, emojiName: String): Result<Boolean>

    suspend fun removeReaction(messageId: Long, emojiName: String): Result<Boolean>
}