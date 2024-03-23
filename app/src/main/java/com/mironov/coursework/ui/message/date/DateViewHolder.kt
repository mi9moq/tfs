package com.mironov.coursework.ui.message.date

import androidx.recyclerview.widget.RecyclerView
import com.mironov.coursework.databinding.DateItemBinding
import com.mironov.coursework.domain.entity.MessageDate
import com.mironov.coursework.ui.utils.toFormatDate

class DateViewHolder(private val binding: DateItemBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(model: MessageDate) {
        binding.date.text = model.date.toFormatDate()
    }
}