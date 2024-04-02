package com.mironov.coursework.ui.message.date

import android.view.LayoutInflater
import android.view.ViewGroup
import com.mironov.coursework.databinding.DateItemBinding
import com.mironov.coursework.ui.adapter.ListItemAdapterDelegate
import com.mironov.coursework.ui.adapter.DelegateItem

class DateDelegate : ListItemAdapterDelegate<DateDelegateItem, DelegateItem, DateViewHolder>() {

    override fun onBindViewHolder(item: DateDelegateItem, holder: DateViewHolder) {
        holder.bind(item.content())
    }

    override fun onCreateViewHolder(parent: ViewGroup): DateViewHolder = DateViewHolder(
        DateItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun isOfViewType(item: DelegateItem): Boolean =
        item is DateDelegateItem
}