package com.mironov.coursework.domain.usecase

import com.mironov.coursework.domain.repository.ChannelRepository
import javax.inject.Inject

class CreateChannelUseCase @Inject constructor(
    private val repository: ChannelRepository
) {

    suspend operator fun invoke(name: String, description: String) =
        repository.createChannel(name, description)
}