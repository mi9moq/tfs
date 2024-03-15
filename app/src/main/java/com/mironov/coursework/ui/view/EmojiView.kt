package com.mironov.coursework.ui.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Rect
import android.text.TextPaint
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import androidx.core.content.withStyledAttributes
import com.mironov.coursework.R

class EmojiView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defTheme: Int = 0,
    defStyle: Int = 0
) : View(context, attributeSet, defStyle, defTheme) {

    var emoji: String = "\uD83E\uDD28"
        set(value) {
            if (field != value) {
                field = value
                requestLayout()
            }
        }
    var count: Int = 99
        set(value) {
            if (field != value) {
                field = value
                requestLayout()
            }
        }

    init {
        context.withStyledAttributes(attributeSet, R.styleable.EmojiView) {
            getInt(R.styleable.EmojiView_count, 0)
        }
    }

    private val textToDraw
        get() = "$emoji $count"

    private val textPaint = TextPaint().apply {
        color = Color.WHITE
        textSize = 14f.sp(context)
    }

    private val textRect = Rect()

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        textPaint.getTextBounds(textToDraw, 0, textToDraw.length, textRect)

        val actualWith = resolveSize(
            paddingRight + paddingLeft + textRect.width(),
            widthMeasureSpec
        )
        val actualHeight = resolveSize(
            paddingTop + paddingBottom + textRect.width(),
            heightMeasureSpec
        )

        setMeasuredDimension(actualWith, actualHeight)
    }

    override fun onDraw(canvas: Canvas) {
        val topOffset = height / 2 - textRect.exactCenterY()

        canvas.drawText(textToDraw, paddingLeft.toFloat(), topOffset, textPaint)
    }

    private fun Float.sp(context: Context) = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_SP,
        this,
        context.resources.displayMetrics
    )
}