package com.mironov.coursework.ui.chat.received

import androidx.recyclerview.widget.RecyclerView
import com.mironov.coursework.databinding.ReceivedMessageItemBinding
import com.mironov.coursework.domain.entity.Message

class ReceivedViewHolder(
    private val binding: ReceivedMessageItemBinding,
    private val chooseReaction: (Long) -> Unit,
    private val onReactionClickListener: (id: Long, emoji: String, isSelected: Boolean) -> Unit,
    private val onMessageLongClickListener: (Message) -> Unit,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(model: Message) {
        with(binding.main) {
            setMessage(model)
            setAvatar(model.avatarUrl)
            setOnAddClickListener(chooseReaction)
            setOnReactionsClickListeners(onReactionClickListener)
        }
        itemView.setOnLongClickListener {
            onMessageLongClickListener(model)
            true
        }
    }
}