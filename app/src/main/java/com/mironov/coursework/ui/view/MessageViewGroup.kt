package com.mironov.coursework.ui.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.withStyledAttributes
import com.mironov.coursework.R
import com.mironov.coursework.databinding.MessageViewgroupBinding
import com.mironov.coursework.ui.utils.layoutWithMargins
import com.mironov.coursework.ui.utils.measureHeightWithMargins
import com.mironov.coursework.ui.utils.measureWidthWithMargins

class MessageViewGroup @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defTheme: Int = 0,
    defStyle: Int = 0
) : ViewGroup(context, attributeSet, defStyle, defTheme) {

    private val avatar: ImageView
    private val userName: TextView
    private val message: TextView
    private val reactions: FlexboxLayout

    private val messageMarginLeft: Int
    private val messageMarginRight: Int
    private val messageMarginTop: Int
    private val messageMarginBottom: Int
    private val messageRound: Float

    var messageBackgroundColor: Int = Color.WHITE
        set(value) {
            if (field != value) {
                field = value
                requestLayout()
            }
        }

    init {
        context.withStyledAttributes(attributeSet, R.styleable.MessageViewGroup) {
            messageBackgroundColor =
                getColor(R.styleable.MessageViewGroup_message_background, Color.BLUE)
        }
        val binding = MessageViewgroupBinding.inflate(LayoutInflater.from(context), this)
        avatar = binding.avatar
        userName = binding.userName
        message = binding.message
        reactions = binding.reactions
        messageMarginLeft = resources.getDimensionPixelSize(R.dimen.message_margin_left)
        messageMarginRight = resources.getDimensionPixelSize(R.dimen.message_margin_right)
        messageMarginTop = resources.getDimensionPixelSize(R.dimen.message_margin_top)
        messageMarginBottom = resources.getDimensionPixelSize(R.dimen.message_margin_bottom)
        messageRound = resources.getDimensionPixelSize(R.dimen.message_round).toFloat()
        setWillNotDraw(false)
    }

    private val backgroundRect = RectF()
    private val backgroundPaint = Paint().apply {
        style = Paint.Style.FILL
        color = messageBackgroundColor
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var offsetX = paddingLeft
        var offsetY = paddingTop
        measureChildWithMargins(avatar, widthMeasureSpec, offsetX, heightMeasureSpec, offsetY)
        offsetX += avatar.measureWidthWithMargins()

        val backgroundRectLeft = offsetX.toFloat() + messageMarginRight
        val backgroundRectTop = offsetY.toFloat()

        measureChildWithMargins(userName, widthMeasureSpec, offsetX, heightMeasureSpec, offsetY)
        var maxWidth = userName.measureWidthWithMargins()
        offsetY += userName.measureHeightWithMargins()

        measureChildWithMargins(message, widthMeasureSpec, offsetX, heightMeasureSpec, offsetY)
        maxWidth = maxOf(maxWidth, message.measureWidthWithMargins())
        offsetY += message.measureHeightWithMargins()

        val backgroundRectRight = maxWidth.toFloat() + backgroundRectLeft + messageMarginRight
        val backgroundRectBottom =
            offsetY.toFloat() + backgroundRectTop + messageMarginBottom + messageMarginTop

        backgroundRect.set(
            backgroundRectLeft,
            backgroundRectTop,
            backgroundRectRight,
            backgroundRectBottom
        )

        measureChildWithMargins(reactions, widthMeasureSpec, offsetX, heightMeasureSpec, offsetY)
        offsetY += reactions.measureHeightWithMargins()
        maxWidth = maxOf(maxWidth, reactions.measureWidthWithMargins())

        val actualWidth = offsetX + maxWidth + messageMarginLeft + messageMarginRight
        val actualHeight = offsetY + paddingBottom + messageMarginTop + messageMarginBottom
        setMeasuredDimension(actualWidth, actualHeight)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        var offsetX = paddingLeft
        var offsetY = paddingTop

        avatar.layoutWithMargins(offsetX, offsetY)
        offsetX += avatar.measureWidthWithMargins()

        userName.layoutWithMargins(offsetX + messageMarginLeft, offsetY + messageMarginTop)
        offsetY += userName.measureHeightWithMargins()

        message.layoutWithMargins(offsetX + messageMarginLeft, offsetY + messageMarginTop)
        offsetY += message.measureHeightWithMargins()

        reactions.layoutWithMargins(
            offsetX, offsetY + messageMarginBottom + messageMarginTop
        )
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawRoundRect(
            backgroundRect,
            messageRound,
            messageRound,
            backgroundPaint
        )
    }

    fun addReaction(emoji: Int, count: Int, isSelected: Boolean) {
        val emojiView = EmojiView(context).apply {
            this.emoji = emoji
            this.reactionsCount = count
            this.isSelected = isSelected
            setBackgroundResource(R.drawable.emoji_bg)
        }
        reactions.addView(emojiView)
        requestLayout()
    }

    override fun generateLayoutParams(attrs: AttributeSet?): LayoutParams {
        return MarginLayoutParams(context, attrs)
    }

    override fun generateDefaultLayoutParams(): LayoutParams {
        return MarginLayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
    }

    fun setAvatar(resId: Int) {
        avatar.setImageResource(resId)
        requestLayout()
    }

    fun setMessage(name: String, message: String) {
        userName.text = name
        this.message.text = message
    }

    override fun generateLayoutParams(p: LayoutParams?): LayoutParams {
        return MarginLayoutParams(p)
    }

    override fun checkLayoutParams(p: LayoutParams): Boolean {
        return p is MarginLayoutParams
    }

    fun setOnAddClickListener(listener: () -> Unit) {
        reactions.setOnAddClickListener {
            listener()
        }
    }
}