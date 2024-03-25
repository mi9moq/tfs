package com.mironov.coursework.domain.entity

data class Reaction (
    val emojiUnicode: Int,
    val count: Int = 0,
    val userId: Int = -1,
    val isSelected: Boolean = false
)