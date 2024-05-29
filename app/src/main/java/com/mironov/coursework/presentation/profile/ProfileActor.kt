package com.mironov.coursework.presentation.profile

import com.mironov.coursework.domain.repository.Result
import com.mironov.coursework.domain.usecase.GetOwnProfileUseCase
import com.mironov.coursework.domain.usecase.GetUserByIdUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import vivid.money.elmslie.core.store.Actor
import javax.inject.Inject

class ProfileActor @Inject constructor(
    private val getUserByIdUseCase: GetUserByIdUseCase,
    private val getOwnProfileUseCase: GetOwnProfileUseCase,
) : Actor<ProfileCommand, ProfileEvent>() {

    override fun execute(command: ProfileCommand): Flow<ProfileEvent> = flow {
        when (command) {
            ProfileCommand.LoadOwnProfile -> {
                when (val result = getOwnProfileUseCase()) {
                    is Result.Failure -> emit(ProfileEvent.Domain.LoadingFailure)
                    is Result.Success -> emit(ProfileEvent.Domain.LoadingSuccess(result.content))
                }
            }

            is ProfileCommand.LoadProfileById -> {
                when (val result = getUserByIdUseCase(command.id)) {
                    is Result.Failure -> emit(ProfileEvent.Domain.LoadingFailure)
                    is Result.Success -> emit(ProfileEvent.Domain.LoadingSuccess(result.content))
                }
            }
        }
    }
}