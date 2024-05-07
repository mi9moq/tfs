package com.mironov.coursework.ui.chat.received

import android.view.LayoutInflater
import android.view.ViewGroup
import com.mironov.coursework.databinding.ReceivedMessageItemBinding
import com.mironov.coursework.ui.adapter.ListItemAdapterDelegate
import com.mironov.coursework.ui.adapter.DelegateItem

class ReceivedDelegate(
    private val chooseReaction: (Long) -> Unit,
    private val changeReaction: (id: Long, emoji: String, isSelected: Boolean) -> Unit,
) : ListItemAdapterDelegate<ReceivedDelegateItem, DelegateItem, ReceivedViewHolder>() {

    override fun onBindViewHolder(item: ReceivedDelegateItem, holder: ReceivedViewHolder) {
        holder.bind(item.content())
    }

    override fun onCreateViewHolder(parent: ViewGroup): ReceivedViewHolder = ReceivedViewHolder(
        ReceivedMessageItemBinding.inflate(LayoutInflater.from(parent.context), parent, false),
        chooseReaction = chooseReaction,
        onReactionClickListener = changeReaction
    )

    override fun isOfViewType(item: DelegateItem): Boolean =
        item is ReceivedDelegateItem
}