package com.mironov.coursework.domain.repository

import com.mironov.coursework.domain.entity.Channel
import com.mironov.coursework.domain.entity.Topic

interface ChannelRepository {

    suspend fun gelSubscribeChannels() : Result<List<Channel>>

    suspend fun gelAllChannels() : Result<List<Channel>>

    suspend fun getTopics(channel: Channel): Result<List<Topic>>
}