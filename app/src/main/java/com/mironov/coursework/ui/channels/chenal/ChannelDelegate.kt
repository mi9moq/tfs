package com.mironov.coursework.ui.channels.chenal

import android.view.LayoutInflater
import android.view.ViewGroup
import com.mironov.coursework.databinding.ChannelItemBinding
import com.mironov.coursework.domain.entity.Channel
import com.mironov.coursework.ui.adapter.DelegateItem
import com.mironov.coursework.ui.adapter.ListItemAdapterDelegate

class ChannelDelegate(
    private val onArrowDownCLicked: (Channel) -> Unit,
    private val onArrowUpClicked: (Int) -> Unit,
    private val onChannelClicked: (String) -> Unit
) : ListItemAdapterDelegate<ChannelDelegateItem, DelegateItem, ChannelViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup): ChannelViewHolder = ChannelViewHolder(
        ChannelItemBinding.inflate(LayoutInflater.from(parent.context), parent, false),
        onArrowDownCLicked,
        onArrowUpClicked,
        onChannelClicked,
    )

    override fun onBindViewHolder(item: ChannelDelegateItem, holder: ChannelViewHolder) {
        holder.bind(item.content())
    }

    override fun isOfViewType(item: DelegateItem): Boolean =
        item is ChannelDelegateItem
}