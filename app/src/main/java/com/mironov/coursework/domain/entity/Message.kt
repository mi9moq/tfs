package com.mironov.coursework.domain.entity

data class Message(
    val avatarUrl: String? = null,
    val content: String,
    val id: Long,
    val isMeMessage: Boolean = false,
    val senderName: String,
    val senderId: Int,
    val sendTime: Long,
    val reactions: Map<Reaction, ReactionCondition>,
    val topicName: String = ""
)