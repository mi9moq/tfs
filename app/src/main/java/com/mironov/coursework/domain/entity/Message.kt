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
) {

    companion object {

        const val MESSAGE_CONTENT_EDITABLE_MINUTES = 1440
        const val MESSAGE_TOPIC_EDITABLE_MINUTES = 10080
        const val MESSAGE_CAN_DELETED_MINUTES = 10
    }
}