package com.mironov.coursework.ui.view

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import androidx.core.content.withStyledAttributes
import androidx.core.view.children
import com.mironov.coursework.R
import com.mironov.coursework.ui.utils.dp

class FlexboxLayout @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defTheme: Int = 0,
    defStyle: Int = 0
) : ViewGroup(context, attributeSet, defStyle, defTheme) {

    var rowMargin = 0
        set(value) {
            if (value != field){
                field = value.toFloat().dp(context).toInt()
                requestLayout()
            }
        }

    var columnMargin = 0
        set(value) {
            if (value != field){
                field = value.toFloat().dp(context).toInt()
                requestLayout()
            }
        }

    init {
        context.withStyledAttributes(attributeSet, R.styleable.FlexboxLayout) {
            rowMargin = getInt(R.styleable.FlexboxLayout_row_margin,0)
            columnMargin = getInt(R.styleable.FlexboxLayout_column_margin,0)
        }
    }

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
                topInRow += maxChildHeight + columnMargin
                actualHeight += topInRow + maxChildHeight + columnMargin
            }
            child.apply {
                left = rowWidth
                top = topInRow
                right = rowWidth + measuredWidth
                bottom = top + measuredHeight
            }
            rowWidth += child.measuredWidth + rowMargin
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