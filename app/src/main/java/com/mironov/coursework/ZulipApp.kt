package com.mironov.coursework

import android.app.Application
import com.mironov.coursework.di.app.AppComponent
import com.mironov.coursework.di.app.DaggerAppComponent

open class ZulipApp: Application() {

    val component by lazy {
        createComponent()
    }

    open fun createComponent(): AppComponent =
        DaggerAppComponent.factory().create(this)

    override fun onCreate() {
        component.inject(this)
        super.onCreate()
    }
}