package com.mironov.coursework.domain.entity

data class Reaction (
    val emojiUnicode: Int,
    val count: Int,
    val userId: Int
)