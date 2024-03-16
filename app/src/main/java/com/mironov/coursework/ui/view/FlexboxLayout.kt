package com.mironov.coursework.ui.view

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import androidx.core.view.children

class FlexboxLayout @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defTheme: Int = 0,
    defStyle: Int = 0
) : ViewGroup(context, attributeSet, defStyle, defTheme) {

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val parentWidth = MeasureSpec.getSize(widthMeasureSpec)
        var actualWidth = 0
        var topInRow = 0
        var actualHeight = 0
        var rowWidth = 0

        val maxChildHeight = if (childCount > 0) {
            children.maxOf {
                it.measuredHeight
            }
        } else 0

        children.forEach { child ->
            measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, 0)
        }

        children.forEach { child ->
            if (rowWidth + child.measuredWidth >= parentWidth) {
                rowWidth = 0
                topInRow += maxChildHeight + 8
                actualHeight += topInRow + maxChildHeight + 8
            }
            child.apply {
                left = rowWidth
                top = topInRow
                right = rowWidth + measuredWidth
                bottom = top + measuredHeight
            }
            rowWidth += child.measuredWidth + 10
            actualWidth = maxOf(actualWidth, rowWidth)
        }

        setMeasuredDimension(
            resolveSize(actualWidth, widthMeasureSpec),
            resolveSize(maxOf(maxChildHeight, actualHeight), heightMeasureSpec)
        )
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        children.forEach { child ->
            child.layout(child.left, child.top, child.right, child.bottom)
        }
    }

    override fun generateLayoutParams(attrs: AttributeSet?): LayoutParams {
        return MarginLayoutParams(context, attrs)
    }

    override fun generateDefaultLayoutParams(): LayoutParams =
        MarginLayoutParams(MATCH_PARENT, WRAP_CONTENT)
}