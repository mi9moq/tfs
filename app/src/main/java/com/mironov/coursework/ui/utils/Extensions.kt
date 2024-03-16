package com.mironov.coursework.ui.utils

import android.content.Context
import android.util.TypedValue

fun Float.sp(context: Context) = TypedValue.applyDimension(
    TypedValue.COMPLEX_UNIT_SP,
    this,
    context.resources.displayMetrics
)