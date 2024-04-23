package com.mironov.coursework.domain.entity

data class Channel(
    val id: Int,
    val name: String,
    val isOpen: Boolean = false,
    val topics: List<Topic> = emptyList()
)