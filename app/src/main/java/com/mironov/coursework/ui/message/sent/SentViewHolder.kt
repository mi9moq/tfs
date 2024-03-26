package com.mironov.coursework.ui.message.sent

import androidx.recyclerview.widget.RecyclerView
import com.mironov.coursework.databinding.SentMessageItemBinding
import com.mironov.coursework.domain.entity.Message

class SentViewHolder(
    private val binding: SentMessageItemBinding,
    private val addReaction: (Int) -> Unit,
    private val onReactionClickListener: (Int, Int) -> Unit,
) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(model: Message) {
        with(binding.main) {
            setMessage(model)
            setOnMessageLongClickListener(addReaction)
            setOnAddClickListener(addReaction)
            setOnReactionsClickListeners(onReactionClickListener)
        }
    }
}