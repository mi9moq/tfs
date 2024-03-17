package com.mironov.coursework.ui.utils

import android.content.Context
import android.util.TypedValue
import android.view.View
import androidx.core.view.marginBottom
import androidx.core.view.marginLeft
import androidx.core.view.marginRight
import androidx.core.view.marginTop

fun Float.sp(context: Context) = TypedValue.applyDimension(
    TypedValue.COMPLEX_UNIT_SP,
    this,
    context.resources.displayMetrics
)

fun Float.dp(context: Context) = TypedValue.applyDimension(
    TypedValue.COMPLEX_UNIT_DIP,
    this,
    context.resources.displayMetrics
)

fun View.measureHeightWithMargins(): Int =
    measuredHeight + marginTop + marginBottom

fun View.measureWidthWithMargins(): Int =
    measuredWidth + marginLeft + marginRight

fun View.layoutWithMargins(left: Int, top: Int) {
    val x = left + marginLeft
    val y = top + marginTop
    layout(x, y, x + measuredWidth, y + measuredHeight)
}