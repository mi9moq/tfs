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
import androidx.core.view.children
import androidx.core.view.isVisible
import com.mironov.coursework.R
import com.mironov.coursework.databinding.MessageViewgroupBinding
import com.mironov.coursework.domain.entity.Message
import com.mironov.coursework.domain.entity.Reaction
import com.mironov.coursework.ui.utils.layoutWithMargins
import com.mironov.coursework.ui.utils.measureHeightWithMargins
import com.mironov.coursework.ui.utils.measureWidthWithMargins

class MessageViewGroup @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defTheme: Int = 0,
    defStyle: Int = 0
) : ViewGroup(context, attributeSet, defStyle, defTheme) {

    companion object {

        private const val UNDEFINED_ID = -1
    }

    var messageId = UNDEFINED_ID
        private set

    private val avatar: ImageView
    private val userName: TextView
    private val message: TextView
    private val reactions: FlexboxLayout

    private val messageMarginLeft: Int
    private val messageMarginRight: Int
    private val messageMarginTop: Int
    private val messageMarginBottom: Int
    private val messageRound: Float

    private var isSentMessage: Boolean = false

    var messageBackgroundColor: Int = Color.WHITE
        set(value) {
            if (field != value) {
                field = value
                requestLayout()
            }
        }

    private val binding by lazy {
        MessageViewgroupBinding.inflate(LayoutInflater.from(context), this)
    }

    init {
        context.withStyledAttributes(attributeSet, R.styleable.MessageViewGroup) {
            messageBackgroundColor = getColor(
                R.styleable.MessageViewGroup_message_background,
                Color.BLUE
            )

            isSentMessage = getBoolean(
                R.styleable.MessageViewGroup_is_sent_message,
                false
            )

            if (isSentMessage) {
                binding.avatar.visibility = GONE
                binding.userName.visibility = GONE
            }
        }
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
        children.forEach { child ->
            measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, 0)
        }
        var offsetX = paddingLeft
        var offsetY = paddingTop
        var maxWidth = 0

        if (!isSentMessage) {
            measureChildWithMargins(avatar, widthMeasureSpec, offsetX, heightMeasureSpec, offsetY)
            offsetX += avatar.measureWidthWithMargins()
        }

        offsetX += messageMarginRight
        val backgroundRectLeft = offsetX.toFloat()
        val backgroundRectTop = offsetY.toFloat()

        offsetY += messageMarginTop

        if (!isSentMessage) {
            measureChildWithMargins(userName, widthMeasureSpec, offsetX, heightMeasureSpec, offsetY)
            offsetY += userName.measureHeightWithMargins()
            maxWidth = userName.measureWidthWithMargins()
        }

        measureChildWithMargins(message, widthMeasureSpec, offsetX, heightMeasureSpec, offsetY)
        offsetY += message.measureHeightWithMargins() + messageMarginBottom
        maxWidth = maxOf(maxWidth, message.measureWidthWithMargins())

        val backgroundRectRight = backgroundRectLeft + maxWidth.toFloat() + messageMarginRight
        val backgroundRectBottom = offsetY.toFloat() + backgroundRectTop

        backgroundRect.set(
            backgroundRectLeft,
            backgroundRectTop,
            backgroundRectRight,
            backgroundRectBottom
        )

        offsetX -= messageMarginLeft

        if (reactions.iconAdd.isVisible) {
            measureChildWithMargins(
                reactions,
                widthMeasureSpec,
                offsetX,
                heightMeasureSpec,
                offsetY
            )
            maxWidth = maxOf(maxWidth, reactions.measureWidthWithMargins())
            offsetY += reactions.measureHeightWithMargins()
        }
        val actualWidth = offsetX + maxWidth + messageMarginLeft + messageMarginRight
        val actualHeight = offsetY + paddingBottom

        setMeasuredDimension(actualWidth, actualHeight)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        if (isSentMessage) {
            layoutSent()
        } else {
            layoutReceived()
        }
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

    fun addReaction(reaction: Reaction) {
        val emojiView = EmojiView(context).apply {
            this.emoji = reaction.emojiUnicode
            this.reactionsCount = reaction.count
            this.isSelected = reaction.isSelected
            setBackgroundResource(R.drawable.emoji_bg)
        }
        reactions.addView(emojiView)
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

    fun setMessage(messageEntity: Message) {
        messageId = messageEntity.id
        message.text = messageEntity.content
        userName.text = messageEntity.senderName
        reactions.removeAllViews()
        if (messageEntity.reactions.isEmpty()) {
            reactions.iconAdd.visibility = INVISIBLE
        } else {
            messageEntity.reactions.forEach { reaction ->
                addReaction(reaction)
            }
            reactions.iconAdd.visibility = VISIBLE
        }
    }

    override fun generateLayoutParams(p: LayoutParams?): LayoutParams {
        return MarginLayoutParams(p)
    }

    override fun checkLayoutParams(p: LayoutParams): Boolean {
        return p is MarginLayoutParams
    }

    fun setOnAddClickListener(listener: (Int) -> Unit) {
        reactions.setOnAddClickListener {
            listener(messageId)
        }
    }

    fun setOnMessageLongClickListener(listener: (Int) -> Unit) {
        userName.setOnLongClickListener {
            listener(messageId)
            true
        }
        message.setOnLongClickListener {
            listener(messageId)
            true
        }
    }

    fun setOnReactionsClickListeners(listener: (id: Int, emoji: Int) -> Unit) {
        reactions.children.forEach { child ->
            if (child is EmojiView) {
                child.setOnClickListener {
                    listener(messageId, child.emoji)
                }
            }
        }
    }

    private fun layoutSent() {
        var offsetX = paddingLeft
        var offsetY = paddingTop + messageMarginTop

        offsetX += measuredWidth - message.measureWidthWithMargins() - messageMarginRight

        message.layoutWithMargins(offsetX, offsetY)
        offsetY += message.measureHeightWithMargins()

        val rectTop = backgroundRect.top
        val rectBottom = backgroundRect.bottom
        val rectLeft = offsetX.toFloat()
        val rectRight = rectLeft +
                message.measureWidthWithMargins() +
                messageMarginLeft

        backgroundRect.set(
            rectLeft,
            rectTop,
            rectRight,
            rectBottom
        )

        offsetX -= measuredWidth - message.measureWidthWithMargins() - messageMarginRight

        if (reactions.iconAdd.isVisible) {
            offsetX = measuredWidth - reactions.measureWidthWithMargins()
            reactions.layoutWithMargins(
                offsetX, offsetY + messageMarginBottom
            )
        }
    }

    private fun layoutReceived() {
        var offsetX = paddingLeft
        var offsetY = paddingTop

        avatar.layoutWithMargins(offsetX, offsetY)
        offsetX += avatar.measureWidthWithMargins()

        userName.layoutWithMargins(offsetX + messageMarginLeft, offsetY + messageMarginTop)
        offsetY += userName.measureHeightWithMargins()

        message.layoutWithMargins(offsetX + messageMarginLeft, offsetY + messageMarginTop)
        offsetY += message.measureHeightWithMargins()

        if (reactions.iconAdd.isVisible) {
            reactions.layoutWithMargins(
                offsetX, offsetY + messageMarginBottom + messageMarginTop
            )
        }
    }
}