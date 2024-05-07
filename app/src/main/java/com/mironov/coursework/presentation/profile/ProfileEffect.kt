package com.mironov.coursework.presentation.profile

sealed interface ProfileEffect {

    data object Error : ProfileEffect
}