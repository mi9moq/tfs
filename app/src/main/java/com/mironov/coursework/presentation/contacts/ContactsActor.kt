package com.mironov.coursework.presentation.contacts

import com.mironov.coursework.di.app.annotation.DefaultDispatcher
import com.mironov.coursework.domain.entity.User
import com.mironov.coursework.domain.repository.Result
import com.mironov.coursework.domain.usecase.GetAllUsersUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import vivid.money.elmslie.core.store.Actor
import javax.inject.Inject

class ContactsActor @Inject constructor(
    private val getAllUsersUseCase: GetAllUsersUseCase,
    @DefaultDispatcher private val dispatcher: CoroutineDispatcher
) : Actor<ContactsCommand, ContactsEvent>() {

    private val cache = mutableListOf<User>()

    override fun execute(command: ContactsCommand): Flow<ContactsEvent> = flow {
        val event = when (command) {
            ContactsCommand.Load -> loadUsers()

            is ContactsCommand.ApplyFilter -> applyFilter(command.query)
        }
        emit(event)
    }

    private suspend fun loadUsers(): ContactsEvent.Domain =
        when (val result = getAllUsersUseCase()) {
            is Result.Success -> {
                cache.clear()
                cache.addAll(result.content)
                ContactsEvent.Domain.LoadingSuccess(result.content)
            }

            is Result.Failure -> {
                ContactsEvent.Domain.LoadingFailure
            }
        }

    private suspend fun applyFilter(query: String): ContactsEvent.Domain =
        withContext(dispatcher) {
            val users = cache.filter {
                it.userName.startsWith(query)
            }
            ContactsEvent.Domain.FilterSuccess(users)
        }
}