package com.mironov.coursework.domain.usecase

import com.mironov.coursework.domain.repository.UserRepository
import javax.inject.Inject

class GetOwnProfileUseCase @Inject constructor(
    private val repository: UserRepository
) {

    suspend operator fun invoke() = repository.getOwnProfile()
}