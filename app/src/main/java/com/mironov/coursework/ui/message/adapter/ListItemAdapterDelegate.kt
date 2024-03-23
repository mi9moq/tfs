package com.mironov.coursework.ui.message.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.ViewHolder

abstract class ListItemAdapterDelegate<I: T, T, VH: ViewHolder>: AdapterDelegate<T> {

    @Suppress("UNCHECKED_CAST")
    override fun onBindViewHolder(holder: ViewHolder, item: T, position: Int) {
        onBindViewHolder((item as I), holder as  VH)
    }

    abstract fun onBindViewHolder(item: I, holder: VH)

    abstract override fun onCreateViewHolder(parent: ViewGroup): VH
}