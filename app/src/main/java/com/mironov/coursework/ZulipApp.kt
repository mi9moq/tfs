package com.mironov.coursework

import android.app.Application
import com.mironov.coursework.di.app.DaggerAppComponent

class ZulipApp: Application() {

    val component by lazy {
        DaggerAppComponent.factory().create(this)
    }

    override fun onCreate() {
        component.inject(this)
        super.onCreate()
    }
}