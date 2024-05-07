package com.mironov.coursework.domain.usecase

import com.mironov.coursework.domain.repository.ReactionRepository
import javax.inject.Inject

class RemoveReactionUseCase @Inject constructor(
    private val repository: ReactionRepository
) {

    suspend operator fun invoke(messageId: Long, emojiName: String) =
        repository.removeReaction(messageId, emojiName)
}