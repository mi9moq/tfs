package com.mironov.coursework.ui.chat.sent

import androidx.recyclerview.widget.RecyclerView
import com.mironov.coursework.databinding.SentMessageItemBinding
import com.mironov.coursework.domain.entity.Message

class SentViewHolder(
    private val binding: SentMessageItemBinding,
    private val chooseReaction: (Long) -> Unit,
    private val onReactionClickListener: (id: Long, emoji: String, isSelected: Boolean) -> Unit,
    private val onMessageLongClickListener: (Message) -> Unit,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(model: Message) {
        with(binding.main) {
            setMessage(model)
            //setOnMessageLongClickListener(chooseReaction)
            setOnAddClickListener(chooseReaction)
            setOnReactionsClickListeners(onReactionClickListener)
        }
        itemView.setOnLongClickListener {
            onMessageLongClickListener(model)
            true
        }
    }
}