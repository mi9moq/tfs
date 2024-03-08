package com.mironov.hw1.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mironov.hw1.R
import com.mironov.hw1.databinding.ItemContactBinding
import com.mironov.hw1.model.Contact

class ContactViewHolder(
    parent: ViewGroup
) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.item_contact, parent, false)
) {

    private val binding = ItemContactBinding.bind(itemView)

    fun bind(contact: Contact) {
        with(binding) {
            phone.text = contact.phone
            name.text = contact.name
        }
    }
}