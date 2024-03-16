package com.mironov.coursework.ui.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Rect
import android.text.TextPaint
import android.util.AttributeSet
import android.view.View
import androidx.core.content.withStyledAttributes
import com.mironov.coursework.R
import com.mironov.coursework.ui.utils.sp

class EmojiView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defTheme: Int = 0,
    defStyle: Int = 0
) : View(context, attributeSet, defStyle, defTheme) {

    companion object {

        private const val DEFAULT_TEST_SIZE = 14f
    }

    var emoji: Int = 0x1F916
        set(value) {
            if (field != value) {
                field = value
                requestLayout()
            }
        }
    var reactionsCount: Int = 0
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
        get() = "${String(Character.toChars(emoji))} $reactionsCount"

    private val textPaint = TextPaint().apply {
        color = Color.WHITE
        textSize = DEFAULT_TEST_SIZE.sp(context)
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
}