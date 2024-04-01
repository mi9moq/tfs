package com.mironov.coursework.ui.contatcs

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.mironov.coursework.domain.entity.Contact

class ContactsAdapter(
    private val onItemClick: (Int)->Unit
): ListAdapter<Contact,ContactViewHolder>(ContactDiffUtilCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder =
        ContactViewHolder(parent, onItemClick)

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}