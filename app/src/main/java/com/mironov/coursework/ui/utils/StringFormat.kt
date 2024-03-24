package com.mironov.coursework.ui.utils

import java.time.LocalDate
import java.time.format.DateTimeFormatter

fun LocalDate.toFormatDate(): String =
    format(DateTimeFormatter.ofPattern("dd MMM"))