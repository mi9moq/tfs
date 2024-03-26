package com.mironov.coursework.ui.message.sent

import android.view.LayoutInflater
import android.view.ViewGroup
import com.mironov.coursework.databinding.SentMessageItemBinding
import com.mironov.coursework.ui.message.adapter.ListItemAdapterDelegate
import com.mironov.coursework.ui.message.adapter.DelegateItem

class SentDelegate(
    private val addReaction: (Int) -> Unit,
    private val onReactionClickListener: (id: Int, emoji: Int) -> Unit,
) : ListItemAdapterDelegate<SentDelegateItem, DelegateItem, SentViewHolder>() {

    override fun onBindViewHolder(item: SentDelegateItem, holder: SentViewHolder) {
        holder.bind(item.content())
    }

    override fun onCreateViewHolder(parent: ViewGroup): SentViewHolder = SentViewHolder(
        SentMessageItemBinding.inflate(LayoutInflater.from(parent.context), parent, false),
        addReaction = addReaction,
        onReactionClickListener = onReactionClickListener
    )

    override fun isOfViewType(item: DelegateItem): Boolean =
        item is SentDelegateItem
}