package com.mironov.coursework.domain.usecase

import com.mironov.coursework.domain.repository.MessageRepository
import javax.inject.Inject

class GetNextMessagesUseCase @Inject constructor(
    private val repository: MessageRepository
) {
    suspend operator fun invoke(channelName: String, topicName: String, anchorMessageId: String) =
        repository.getNextMessages(channelName, topicName, anchorMessageId)
}