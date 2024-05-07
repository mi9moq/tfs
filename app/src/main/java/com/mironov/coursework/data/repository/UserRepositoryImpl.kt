package com.mironov.coursework.data.repository

import com.mironov.coursework.data.mapper.MY_ID
import com.mironov.coursework.data.mapper.toEntity
import com.mironov.coursework.data.network.api.ZulipApi
import com.mironov.coursework.data.utils.runCatchingNonCancellation
import com.mironov.coursework.di.app.annotation.IoDispatcher
import com.mironov.coursework.domain.entity.User
import com.mironov.coursework.domain.repository.Result
import com.mironov.coursework.domain.repository.UserRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val api: ZulipApi,
    @IoDispatcher private val dispatcher: CoroutineDispatcher,
) : UserRepository {

    override suspend fun getAll(): Result<List<User>> = withContext(dispatcher) {
        runCatchingNonCancellation {
            val presences = api.getAllUserStatus().presences
            val users = api.getAllUsersProfile().users.filter { !it.isBot }.map {
                val currentPresence = presences[it.email]?.toEntity() ?: User.Presence.OFFLINE
                it.toEntity(currentPresence)
            }
            Result.Success(users)
        }
    }

    override suspend fun getById(userId: Int): Result<User> = withContext(dispatcher) {
        runCatchingNonCancellation {
            val presence = api.getUserStatusById(userId).presences.toEntity()
            val user = api.getUserById(userId).user.toEntity(presence)
            Result.Success(user)
        }
    }

    override suspend fun getOwnProfile(): Result<User> = withContext(dispatcher) {
        runCatchingNonCancellation {
            val presence = api.getUserStatusById(MY_ID).presences.toEntity()
            val user = api.getOwnProfile().toEntity(presence)
            Result.Success(user)
        }
    }
}