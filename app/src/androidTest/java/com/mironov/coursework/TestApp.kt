package com.mironov.coursework

import com.mironov.coursework.di.DaggerTestAppComponent
import com.mironov.coursework.di.app.AppComponent

class TestApp : ZulipApp() {

    override fun createComponent(): AppComponent =
        DaggerTestAppComponent.factory().create(this)
}