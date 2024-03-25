package com.mironov.coursework.ui.message.sent

import androidx.recyclerview.widget.RecyclerView
import com.mironov.coursework.databinding.SentMessageItemBinding
import com.mironov.coursework.domain.entity.Message

class SentViewHolder(
    private val binding: SentMessageItemBinding,
    private val onLongClick: (Int) -> Unit
) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(model: Message) {
        with(binding.main) {
            setMessage(model)
            setOnMessageLongClickListener{
                onLongClick(it)
            }
        }
    }
}