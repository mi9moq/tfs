package com.mironov.coursework.presentation.profile

import com.mironov.coursework.navigation.router.ProfileRouter
import vivid.money.elmslie.core.store.dsl.ScreenDslReducer
import javax.inject.Inject

class ProfileReducer @Inject constructor(
    private val router: ProfileRouter
) : ScreenDslReducer<ProfileEvent,
        ProfileEvent.Ui,
        ProfileEvent.Domain,
        ProfileState,
        ProfileEffect,
        ProfileCommand>(
    ProfileEvent.Ui::class,
    ProfileEvent.Domain::class
) {
    override fun Result.internal(event: ProfileEvent.Domain): Any = when (event) {
        ProfileEvent.Domain.LoadingFailure -> {
            state {
                copy(isLoading = false)
            }
            effects {
                +ProfileEffect.Error
            }
        }

        is ProfileEvent.Domain.LoadingSuccess -> {
            state {
                copy(isLoading = false, user = event.user)
            }
        }
    }

    override fun Result.ui(event: ProfileEvent.Ui): Any = when (event) {
        ProfileEvent.Ui.Initial -> {
            state {
                copy(isLoading = true)
            }
        }

        ProfileEvent.Ui.LoadOwnProfile -> {
            state {
                copy(isLoading = true)
            }
            commands {
                +ProfileCommand.LoadOwnProfile
            }
        }

        is ProfileEvent.Ui.LoadUserById -> {
            state {
                copy(isLoading = true)
            }
            commands {
                +ProfileCommand.LoadProfileById(event.id)
            }
        }

        ProfileEvent.Ui.OnArrowBackClicked -> {
            router.back()
        }
    }
}