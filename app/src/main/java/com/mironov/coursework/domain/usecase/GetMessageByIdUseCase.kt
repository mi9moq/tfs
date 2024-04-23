package com.mironov.coursework.domain.usecase

import com.mironov.coursework.domain.repository.MessageRepository
import javax.inject.Inject

class GetMessageByIdUseCase @Inject constructor(
    private val repository: MessageRepository
) {
    suspend operator fun invoke(id: Int) =
        repository.getMessagesById(id)
}