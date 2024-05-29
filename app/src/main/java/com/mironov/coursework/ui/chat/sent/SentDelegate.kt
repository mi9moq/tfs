package com.mironov.coursework.ui.chat.sent

import android.view.LayoutInflater
import android.view.ViewGroup
import com.mironov.coursework.databinding.SentMessageItemBinding
import com.mironov.coursework.domain.entity.Message
import com.mironov.coursework.ui.adapter.ListItemAdapterDelegate
import com.mironov.coursework.ui.adapter.DelegateItem

class SentDelegate(
    private val chooseReaction: (Long) -> Unit,
    private val changeReaction: (id: Long, emoji: String, isSelected: Boolean) -> Unit,
    private val onMessageLongClickListener: (Message) -> Unit,
) : ListItemAdapterDelegate<SentDelegateItem, DelegateItem, SentViewHolder>() {

    override fun onBindViewHolder(item: SentDelegateItem, holder: SentViewHolder) {
        holder.bind(item.content())
    }

    override fun onCreateViewHolder(parent: ViewGroup): SentViewHolder = SentViewHolder(
        SentMessageItemBinding.inflate(LayoutInflater.from(parent.context), parent, false),
        chooseReaction = chooseReaction,
        onReactionClickListener = changeReaction,
        onMessageLongClickListener = onMessageLongClickListener,
    )

    override fun isOfViewType(item: DelegateItem): Boolean =
        item is SentDelegateItem
}