package com.mironov.coursework.ui.channel

import android.view.LayoutInflater
import android.view.ViewGroup
import com.mironov.coursework.databinding.ChannelItemBinding
import com.mironov.coursework.ui.message.adapter.DelegateItem
import com.mironov.coursework.ui.message.adapter.ListItemAdapterDelegate

class ChannelDelegate(

) : ListItemAdapterDelegate<ChannelDelegateItem, DelegateItem, ChannelViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup): ChannelViewHolder = ChannelViewHolder(
        ChannelItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(item: ChannelDelegateItem, holder: ChannelViewHolder) {
        holder.bind(item.content())
    }

    override fun isOfViewType(item: DelegateItem): Boolean =
        item is ChannelDelegateItem
}