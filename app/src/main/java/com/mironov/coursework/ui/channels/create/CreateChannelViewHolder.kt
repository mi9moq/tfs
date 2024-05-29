package com.mironov.coursework.ui.channels.create

import androidx.recyclerview.widget.RecyclerView
import com.mironov.coursework.databinding.CreateChannelItemBinding

class CreateChannelViewHolder(
    private val binding: CreateChannelItemBinding,
    private val onItemClicked: () -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind() {
        itemView.setOnClickListener {
            onItemClicked()
        }
    }
}