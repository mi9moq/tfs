package com.mironov.coursework.ui.message.received

import android.view.LayoutInflater
import android.view.ViewGroup
import com.mironov.coursework.databinding.ReceivedMessageItemBinding
import com.mironov.coursework.ui.message.adapter.ListItemAdapterDelegate
import com.mironov.coursework.ui.message.adapter.DelegateItem

class ReceivedDelegate(
    private val addReaction: (Int) -> Unit,
    private val onReactionClickListener: (id: Int, emoji: Int) -> Unit,
) : ListItemAdapterDelegate<ReceivedDelegateItem, DelegateItem, ReceivedViewHolder>() {

    override fun onBindViewHolder(item: ReceivedDelegateItem, holder: ReceivedViewHolder) {
        holder.bind(item.content())
    }

    override fun onCreateViewHolder(parent: ViewGroup): ReceivedViewHolder = ReceivedViewHolder(
        ReceivedMessageItemBinding.inflate(LayoutInflater.from(parent.context), parent, false),
        addReaction = addReaction,
        onReactionClickListener = onReactionClickListener
    )

    override fun isOfViewType(item: DelegateItem): Boolean =
        item is ReceivedDelegateItem
}