package com.mironov.coursework.ui.contatcs

import androidx.recyclerview.widget.DiffUtil
import com.mironov.coursework.domain.entity.User

class ContactDiffUtilCallback: DiffUtil.ItemCallback<User>() {

    override fun areItemsTheSame(oldItem: User, newItem: User): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: User, newItem: User): Boolean =
        oldItem == newItem
}