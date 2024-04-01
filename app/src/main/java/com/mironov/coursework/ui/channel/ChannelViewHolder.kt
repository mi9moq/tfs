package com.mironov.coursework.ui.channel

import androidx.recyclerview.widget.RecyclerView
import com.mironov.coursework.databinding.ChannelItemBinding
import com.mironov.coursework.domain.entity.Channel

class ChannelViewHolder(
    private val binding: ChannelItemBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(channel: Channel) {
        binding.channel.text = channel.name

        binding.arrow.setOnClickListener {

        }
    }
}