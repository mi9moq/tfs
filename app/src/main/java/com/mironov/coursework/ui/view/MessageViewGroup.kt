package com.mironov.coursework.ui.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
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

    init {
        val binding = MessageViewgroupBinding.inflate(LayoutInflater.from(context), this)
        avatar = binding.avatar
        userName = binding.userName
        message = binding.message
        reactions = binding.reactions
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var offsetX = paddingLeft
        var offsetY = paddingTop
        measureChildWithMargins(avatar, widthMeasureSpec, offsetX, heightMeasureSpec, offsetY)
        offsetX += avatar.measureWidthWithMargins()

        measureChildWithMargins(userName, widthMeasureSpec, offsetX, heightMeasureSpec, offsetY)
        var maxWidth = userName.measureWidthWithMargins()
        offsetY += userName.measureHeightWithMargins()

        measureChildWithMargins(message, widthMeasureSpec, offsetX, heightMeasureSpec, offsetY)
        maxWidth = maxOf(maxWidth, message.measureWidthWithMargins())
        offsetY += message.measureHeightWithMargins()

        measureChildWithMargins(reactions, widthMeasureSpec, offsetX, heightMeasureSpec, offsetY)
        offsetY += reactions.measureHeightWithMargins()
        maxWidth = maxOf(maxWidth, reactions.measureWidthWithMargins())

        val actualWidth = offsetX + maxWidth
        val actualHeight = offsetY + paddingBottom
        setMeasuredDimension(actualWidth, actualHeight)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        var offsetX = paddingLeft
        var offsetY = paddingTop

        avatar.layoutWithMargins(offsetX, offsetY)
        offsetX += avatar.measureWidthWithMargins()

        userName.layoutWithMargins(offsetX, offsetY)
        offsetY += userName.measureHeightWithMargins()

        message.layoutWithMargins(offsetX, offsetY)
        offsetY += message.measureHeightWithMargins()


        reactions.layoutWithMargins(offsetX, offsetY)
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
}