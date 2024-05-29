package com.mironov.coursework.domain.usecase

import com.mironov.coursework.domain.repository.MessageRepository
import javax.inject.Inject

class EditMessageTopicUseCase @Inject constructor(
    private val repository: MessageRepository
) {
    suspend operator fun invoke(messageId: Long, topic: String) =
        repository.editMessageTopic(messageId, topic)
}