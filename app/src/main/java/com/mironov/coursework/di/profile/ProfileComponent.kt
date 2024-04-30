package com.mironov.coursework.di.profile

import com.mironov.coursework.ui.profile.OwnProfileFragment
import com.mironov.coursework.ui.profile.UserProfileFragment
import dagger.Subcomponent

@ProfileScope
@Subcomponent(
    modules = [
        ProfileModule::class
    ]
)
interface ProfileComponent {

    fun inject(fragment: OwnProfileFragment)

    fun inject(fragment: UserProfileFragment)

    @Subcomponent.Factory
    interface Factory {

        fun create(): ProfileComponent
    }
}