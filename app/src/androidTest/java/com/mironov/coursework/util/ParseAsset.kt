package com.mironov.coursework.util

import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import java.io.BufferedReader
import java.io.InputStreamReader

fun fromAssets(path: String, vararg formatArgs: Any? = emptyArray()): String {
    val assetManager = getInstrumentation().context.assets
    val stringFile = BufferedReader(InputStreamReader(assetManager.open(path), Charsets.UTF_8)).readText()
    return if (formatArgs.isNotEmpty()) {
        stringFile.format(*formatArgs)
    } else {
        stringFile
    }
}