package com.mironov.coursework.domain.usecase

import com.mironov.coursework.domain.repository.ChannelRepository
import javax.inject.Inject

class GetTopicsUseCase @Inject constructor(
    private val repository: ChannelRepository
) {

    suspend operator fun invoke(
        channelId: Int,
        channelName: String
    ) = repository.getTopics(channelId, channelName)
}