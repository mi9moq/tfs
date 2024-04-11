package com.mironov.coursework.ui.chat.sent

import android.view.LayoutInflater
import android.view.ViewGroup
import com.mironov.coursework.databinding.SentMessageItemBinding
import com.mironov.coursework.ui.adapter.ListItemAdapterDelegate
import com.mironov.coursework.ui.adapter.DelegateItem

class SentDelegate(
    private val addReaction: (Long) -> Unit,
    private val onReactionClickListener: (id: Long, emoji: Int) -> Unit,
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