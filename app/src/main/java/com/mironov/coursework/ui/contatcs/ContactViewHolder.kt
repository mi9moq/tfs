package com.mironov.coursework.ui.contatcs

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mironov.coursework.R
import com.mironov.coursework.databinding.ContactItemBinding
import com.mironov.coursework.domain.entity.Contact

class ContactViewHolder(
    parent: ViewGroup,
    private val onItemClick: (Int) -> Unit
) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.contact_item, parent, false)
) {

    private val binding = ContactItemBinding.bind(parent)

    fun bind(contact: Contact) {
        with(binding) {
            userName.text = contact.userName
            email.text = contact.email
            avatar.setImageResource(R.drawable.ic_avatar)//TODO переписать на coil
        }

        itemView.setOnClickListener {
            onItemClick(contact.id)
        }
    }
}