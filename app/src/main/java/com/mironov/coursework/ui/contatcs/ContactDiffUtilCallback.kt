package com.mironov.coursework.ui.contatcs

import androidx.recyclerview.widget.DiffUtil
import com.mironov.coursework.domain.entity.Contact

class ContactDiffUtilCallback: DiffUtil.ItemCallback<Contact>() {

    override fun areItemsTheSame(oldItem: Contact, newItem: Contact): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Contact, newItem: Contact): Boolean =
        oldItem == newItem
}