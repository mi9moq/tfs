package com.mironov.hw1.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mironov.hw1.model.Contact

class ContactAdapter : RecyclerView.Adapter<ContactViewHolder>() {

    private val _items = mutableListOf<Contact>()

    fun setItems(items: ArrayList<Contact>){
        _items.clear()
        _items.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder =
        ContactViewHolder(parent)

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        holder.bind(_items[position])
    }

    override fun getItemCount(): Int = _items.size
}