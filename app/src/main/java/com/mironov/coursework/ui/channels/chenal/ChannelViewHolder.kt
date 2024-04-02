package com.mironov.coursework.ui.channels.chenal

import androidx.recyclerview.widget.RecyclerView
import com.mironov.coursework.R
import com.mironov.coursework.databinding.ChannelItemBinding
import com.mironov.coursework.domain.entity.Channel

class ChannelViewHolder(
    private val binding: ChannelItemBinding,
    private val showTopics: (Int) -> Unit,
    private val hideTopics: (Int) -> Unit,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(channel: Channel) {
        binding.channel.text = channel.name
        if (channel.isOpen) {
            binding.arrow.setImageResource(R.drawable.ic_arrow_up)
        } else {
            binding.arrow.setImageResource(R.drawable.ic_arrow_down)
        }

        itemView.setOnClickListener {
            if (channel.isOpen) {
                hideTopics(channel.id)
            } else {
                showTopics(channel.id)
            }
        }
    }
}