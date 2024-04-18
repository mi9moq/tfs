package com.mironov.coursework.di.module

import com.mironov.coursework.di.AppScope
import com.mironov.coursework.domain.repository.UserRepository
import com.mironov.coursework.data.repository.UserRepositoryImpl
import dagger.Binds
import dagger.Module

@Module
interface UserModule {

    @AppScope
    @Binds
    fun bindUserRepository(impl: UserRepositoryImpl): UserRepository
}