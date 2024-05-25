package com.mironov.coursework.ui.channels.create

import android.view.LayoutInflater
import android.view.ViewGroup
import com.mironov.coursework.databinding.CreateChannelItemBinding
import com.mironov.coursework.ui.adapter.DelegateItem
import com.mironov.coursework.ui.adapter.ListItemAdapterDelegate

class CreateChannelDelegate(
    private val onItemClicked: () -> Unit
) : ListItemAdapterDelegate<CreateChannelDelegateItem, DelegateItem, CreateChannelViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup): CreateChannelViewHolder =
        CreateChannelViewHolder(
            CreateChannelItemBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            onItemClicked
        )

    override fun onBindViewHolder(
        item: CreateChannelDelegateItem,
        holder: CreateChannelViewHolder
    ) = holder.bind()

    override fun isOfViewType(item: DelegateItem): Boolean =
        item is CreateChannelDelegateItem
}