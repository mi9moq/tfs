package com.mironov.coursework.domain.usecase

import com.mironov.coursework.domain.repository.MessageRepository
import javax.inject.Inject

class GetPrevMessagesUseCase @Inject constructor(
    private val repository: MessageRepository
) {
    suspend operator fun invoke(channelName: String, topicName: String, anchorMessageId: Long) =
        repository.getPrevMessages(channelName, topicName, anchorMessageId)
}