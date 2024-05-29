package com.mironov.coursework.presentation.chat

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ChatInfo(
    val topicName: String = EMPTY_STRING,
    val channelName: String = EMPTY_STRING,
    val channelId: Int = UNDEFINED_ID,
) : Parcelable {

    companion object {
        const val EMPTY_STRING = ""
        const val UNDEFINED_ID = -1
    }
}