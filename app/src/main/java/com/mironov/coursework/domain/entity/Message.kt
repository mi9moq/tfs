package com.mironov.coursework.domain.entity

import java.time.LocalDate

data class Message(
    val avatarUrl: String? = null,
    val content: String,
    val id: Long,
    val isMeMessage: Boolean = false,
    val senderName: String,
    val senderId: Long,
    val sendTime: LocalDate,
    val reactions: Map<Reaction, ReactionCondition>
)