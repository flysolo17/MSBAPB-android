package com.danica.msbapb.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

const val STORAGE_LINK ="http://192.168.100.18/msbapb/api/uploads"

fun String.formatDate(): String {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val date = dateFormat.parse(this)
    val formattedDateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
    return formattedDateFormat.format(date)
}