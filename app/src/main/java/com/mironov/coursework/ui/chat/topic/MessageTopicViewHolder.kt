package com.mironov.coursework.ui.chat.topic

import androidx.recyclerview.widget.RecyclerView
import com.mironov.coursework.databinding.ChatTopicItemBinding

class MessageTopicViewHolder(
    private val binding: ChatTopicItemBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(topicName: String) {
        binding.chatTopic.text = topicName
    }
}