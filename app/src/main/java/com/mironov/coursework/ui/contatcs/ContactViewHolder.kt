package com.mironov.coursework.ui.contatcs

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mironov.coursework.R
import com.mironov.coursework.databinding.ContactItemBinding
import com.mironov.coursework.domain.entity.User
import com.mironov.coursework.ui.utils.setCircleAvatar

class ContactViewHolder(
    parent: ViewGroup,
    private val onItemClick: (Int) -> Unit
) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.contact_item, parent, false)
) {

    private val binding = ContactItemBinding.bind(itemView)

    fun bind(contact: User) {
        with(binding) {
            userName.text = contact.userName
            email.text = contact.email
            avatar.setCircleAvatar(contact.avatarUrl)
            when (contact.presence) {
                User.Presence.ACTIVE -> presence.setBackgroundResource(R.drawable.online_shape)
                User.Presence.IDLE -> presence.setBackgroundResource(R.drawable.idle_shape)
                User.Presence.OFFLINE -> presence.setBackgroundResource(R.drawable.offline_shape)
            }
        }

        itemView.setOnClickListener {
            onItemClick(contact.id)
        }
    }
}