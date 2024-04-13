package com.mironov.coursework.ui.contatcs

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.mironov.coursework.R
import com.mironov.coursework.databinding.ContactItemBinding
import com.mironov.coursework.domain.entity.User

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
            avatar.load(contact.avatarUrl){
                transformations(CircleCropTransformation())
                error(R.drawable.ic_avatar)
            }
        }

        itemView.setOnClickListener {
            onItemClick(contact.id)
        }
    }
}