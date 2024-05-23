package com.mironov.coursework.ui.chat.topic

import android.view.LayoutInflater
import android.view.ViewGroup
import com.mironov.coursework.databinding.ChatTopicItemBinding
import com.mironov.coursework.ui.adapter.DelegateItem
import com.mironov.coursework.ui.adapter.ListItemAdapterDelegate

class MessageTopicDelegate : ListItemAdapterDelegate<MessageTopicDelegateItem, DelegateItem, MessageTopicViewHolder>() {

    override fun isOfViewType(item: DelegateItem): Boolean =
        item is MessageTopicDelegateItem

    override fun onBindViewHolder(item: MessageTopicDelegateItem, holder: MessageTopicViewHolder) {
        holder.bind(item.content())
    }

    override fun onCreateViewHolder(parent: ViewGroup): MessageTopicViewHolder = MessageTopicViewHolder(
        binding = ChatTopicItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )
}