package com.mironov.coursework.ui.utils

import android.content.Context
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.mironov.coursework.R
import com.mironov.coursework.domain.entity.User
import java.time.LocalDate
import java.time.format.DateTimeFormatter

fun LocalDate.toFormatDate(): String =
    format(DateTimeFormatter.ofPattern("dd MMM"))

fun TextView.applyPresence(presence: User.Presence, context: Context) {
    when (presence) {
        User.Presence.ACTIVE -> {
            text = context.getString(R.string.online)
            setTextColor(ContextCompat.getColor(context, R.color.online))
        }

        User.Presence.IDLE -> {
            text = context.getString(R.string.idle)
            setTextColor(ContextCompat.getColor(context, R.color.idle))
        }

        User.Presence.OFFLINE -> {
            text = context.getString(R.string.offline)
            setTextColor(ContextCompat.getColor(context, R.color.offline))
        }
    }
}