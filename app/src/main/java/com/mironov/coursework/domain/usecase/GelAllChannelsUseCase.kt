package com.mironov.coursework.domain.usecase

import com.mironov.coursework.domain.repository.ChannelRepository
import javax.inject.Inject

class GelAllChannelsUseCase @Inject constructor(
    private val repository: ChannelRepository
) {

    suspend operator fun invoke() = repository.gelAllChannels()
}