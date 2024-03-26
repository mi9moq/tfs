package com.mironov.coursework.ui.message.received

import androidx.recyclerview.widget.RecyclerView
import com.mironov.coursework.R
import com.mironov.coursework.databinding.ReceivedMessageItemBinding
import com.mironov.coursework.domain.entity.Message

class ReceivedViewHolder(
    private val binding: ReceivedMessageItemBinding,
    private val addReaction: (Int) -> Unit,
    private val onReactionClickListener: (id: Int, emoji: Int) -> Unit,
) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(model: Message) {
        with(binding.main) {
            setMessage(model)
            setAvatar(R.drawable.ic_avatar)
            setOnMessageLongClickListener(addReaction)
            setOnAddClickListener(addReaction)
            setOnReactionsClickListeners(onReactionClickListener)
        }
    }
}