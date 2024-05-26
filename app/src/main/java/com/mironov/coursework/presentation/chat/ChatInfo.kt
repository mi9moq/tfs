package com.mironov.coursework.presentation.chat

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ChatInfo(
    val topicName: String = NO_TOPIC,
    val channelName: String,
    val channelId: Int = UNDEFINED_ID,
) : Parcelable {

    companion object {
        const val NO_TOPIC = ""
        const val UNDEFINED_ID = -1
    }
}