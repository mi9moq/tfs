package com.mironov.coursework.domain.repository

import com.mironov.coursework.domain.entity.User

interface UserRepository {

    suspend fun getAll(): Result<List<User>>

    suspend fun getById(userId: Int): Result<User>

    suspend fun getOwnProfile(): Result<User>
}