package com.mironov.coursework.ui.channels.topic

import android.view.LayoutInflater
import android.view.ViewGroup
import com.mironov.coursework.databinding.TopicItemBinding
import com.mironov.coursework.domain.entity.Topic
import com.mironov.coursework.ui.adapter.DelegateItem
import com.mironov.coursework.ui.adapter.ListItemAdapterDelegate

class TopicDelegate(
    private val onTopicClicked: (Topic) -> Unit
) : ListItemAdapterDelegate<TopicDelegateItem, DelegateItem, TopicViewHolder>() {

    override fun isOfViewType(item: DelegateItem): Boolean =
        item is TopicDelegateItem

    override fun onBindViewHolder(item: TopicDelegateItem, holder: TopicViewHolder) {
        holder.bind(item.content())
    }

    override fun onCreateViewHolder(parent: ViewGroup): TopicViewHolder = TopicViewHolder(
        TopicItemBinding.inflate(LayoutInflater.from(parent.context), parent, false),
        onTopicClicked
    )
}