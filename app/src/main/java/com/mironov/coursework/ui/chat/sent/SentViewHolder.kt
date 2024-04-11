package com.mironov.coursework.ui.chat.sent

import androidx.recyclerview.widget.RecyclerView
import com.mironov.coursework.databinding.SentMessageItemBinding
import com.mironov.coursework.domain.entity.Message

class SentViewHolder(
    private val binding: SentMessageItemBinding,
    private val addReaction: (Long) -> Unit,
    private val onReactionClickListener: (id: Long, emoji: Int) -> Unit,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(model: Message) {
        with(binding.main) {
            setMessage(model)
            setOnMessageLongClickListener(addReaction)
            setOnAddClickListener(addReaction)
            setOnReactionsClickListeners(onReactionClickListener)
        }
    }
}