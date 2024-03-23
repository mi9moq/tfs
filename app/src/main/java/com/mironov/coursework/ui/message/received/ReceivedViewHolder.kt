package com.mironov.coursework.ui.message.received

import androidx.recyclerview.widget.RecyclerView
import com.mironov.coursework.R
import com.mironov.coursework.databinding.ReceivedMessageItemBinding
import com.mironov.coursework.domain.entity.Message

class ReceivedViewHolder(private val binding: ReceivedMessageItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(model: Message) {
        with(binding.main) {
            setMessage(model.content)
            setUsername(model.senderName)
            setAvatar(R.drawable.ic_avatar)
            val emoji = model.reactions[0]
            addReaction(emoji = emoji.emojiUnicode, count = emoji.count, isSelected = false)
        }
    }
}