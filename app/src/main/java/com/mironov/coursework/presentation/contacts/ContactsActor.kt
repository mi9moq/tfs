package com.mironov.coursework.presentation.contacts

import com.mironov.coursework.domain.entity.User
import com.mironov.coursework.domain.repository.Result
import com.mironov.coursework.domain.usecase.GetAllUsersUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import vivid.money.elmslie.core.store.Actor
import javax.inject.Inject

class ContactsActor @Inject constructor(
    private val getAllUsersUseCase: GetAllUsersUseCase,
) : Actor<ContactsCommand, ContactsEvent>() {

    private val cache = mutableListOf<User>()

    override fun execute(command: ContactsCommand): Flow<ContactsEvent> = flow {
        when (command) {
            ContactsCommand.Load -> {
                when (val result = getAllUsersUseCase()) {
                    is Result.Success -> {
                        cache.clear()
                        cache.addAll(result.content)
                        emit(ContactsEvent.Domain.LoadingSuccess(result.content))
                    }

                    is Result.Failure -> {
                        emit(ContactsEvent.Domain.LoadingFailure)
                    }
                }
            }
        }
    }
}