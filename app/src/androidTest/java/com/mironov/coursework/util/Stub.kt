package com.mironov.coursework.util

import com.mironov.coursework.data.mapper.toLocalDate
import com.mironov.coursework.ui.utils.toFormatDate

val firstMessageDate = 1713230866L.toLocalDate().toFormatDate()

const val CHANNEL_NAME_KEY = "channel name"
const val TOPIC_NAME_KEY = "topic name"
const val CHANNEL_NAME = "Test Channel"
const val TOPIC_NAME = "Test topic"