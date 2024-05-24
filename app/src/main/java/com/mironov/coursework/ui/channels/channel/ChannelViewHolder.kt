package com.mironov.coursework.ui.channels.channel

import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.mironov.coursework.R
import com.mironov.coursework.databinding.ChannelItemBinding
import com.mironov.coursework.domain.entity.Channel

class ChannelViewHolder(
    private val binding: ChannelItemBinding,
    private val onArrowDownCLicked: (Channel) -> Unit,
    private val onArrowUpClicked: (Int) -> Unit,
    private val onChannelClicked: (String) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(channel: Channel) {
        with(binding) {
            this.channel.text = String
                .format(itemView.context.getString(R.string.channel_name), channel.name)

            arrowDown.isVisible = !channel.isOpen
            arrowUp.isVisible = channel.isOpen

            arrowDown.setOnClickListener {
                onArrowDownCLicked(channel)
            }
            arrowUp.setOnClickListener {
                onArrowUpClicked(channel.id)
            }
        }

        itemView.setOnClickListener {
            onChannelClicked(channel.name)
        }
    }
}