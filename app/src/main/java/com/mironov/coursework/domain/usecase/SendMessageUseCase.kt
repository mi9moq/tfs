package com.mironov.coursework.domain.usecase

import com.mironov.coursework.domain.repository.MessageRepository
import javax.inject.Inject

class SendMessageUseCase @Inject constructor(
    private val repository: MessageRepository
) {

    suspend operator fun invoke(channelName: String, topicName: String, content: String) =
        repository.sendMessages(channelName, topicName, content)
}