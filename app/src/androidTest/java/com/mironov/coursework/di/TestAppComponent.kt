package com.mironov.coursework.di

import android.content.Context
import com.mironov.coursework.di.app.AppComponent
import com.mironov.coursework.di.app.annotation.AppScope
import com.mironov.coursework.di.app.module.DataSourceModule
import com.mironov.coursework.di.app.module.DatabaseModule
import com.mironov.coursework.di.app.module.DispatcherModule
import com.mironov.coursework.di.app.module.NavigationModule
import com.mironov.coursework.di.app.module.NetworkModule
import com.mironov.coursework.di.app.module.RepositoryModule
import com.mironov.coursework.di.app.module.ViewModelModule
import dagger.BindsInstance
import dagger.Component

@AppScope
@Component(
    modules = [
        NavigationModule::class,
        ViewModelModule::class,
        NetworkModule::class,
        DispatcherModule::class,
        RepositoryModule::class,
        DatabaseModule::class,
        DataSourceModule::class,
        TestBaseUrlModule::class,
    ]
)
interface TestAppComponent: AppComponent {

    @Component.Factory
    interface Factory{
        fun create(
            @BindsInstance context: Context
        ): TestAppComponent
    }
}