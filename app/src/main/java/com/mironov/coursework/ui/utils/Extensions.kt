package com.mironov.coursework.ui.utils

import android.content.Context
import android.util.TypedValue
import android.view.View
import androidx.core.view.isVisible
import androidx.core.view.marginBottom
import androidx.core.view.marginLeft
import androidx.core.view.marginRight
import androidx.core.view.marginTop
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.facebook.shimmer.ShimmerFrameLayout
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.launch

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

fun <T> Fragment.collectStateFlow(flow: Flow<T>, collector: FlowCollector<T>) {
    viewLifecycleOwner.lifecycleScope.launch {
        viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            flow.collect(collector)
        }
    }
}

fun ShimmerFrameLayout.show() {
    isVisible = true
    startShimmer()
}

fun ShimmerFrameLayout.hide() {
    stopShimmer()
    isVisible = false
}