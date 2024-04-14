package com.mironov.coursework.ui.channels.topic

import androidx.recyclerview.widget.RecyclerView
import com.mironov.coursework.databinding.TopicItemBinding
import com.mironov.coursework.domain.entity.Topic

class TopicViewHolder(
    private val binding: TopicItemBinding,
    private val onTopicClicked: (Topic) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(topic: Topic) {

        with(binding) {
            topicName.text = topic.name
            messageCount.text = topic.messageCount.toString()
        }

        itemView.setOnClickListener {
            onTopicClicked(topic)
        }
    }
}