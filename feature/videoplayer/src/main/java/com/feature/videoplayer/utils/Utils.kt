package com.feature.videoplayer.utils

import android.annotation.SuppressLint
import android.icu.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

//fun Long.formatTimeHHMMSS(): String {
//    val date = Date(this)
//    val format = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
//    return format.format(date)
//}

@SuppressLint("DefaultLocale")
fun Long.formatTimeHHMMSS(): String {
    val totalSeconds = this / 1000
    val seconds = totalSeconds % 60
    val minutes = (totalSeconds / 60) % 60
    val hours = totalSeconds / 3600

    return if (hours > 0) {
        String.format("%02d:%02d:%02d", hours, minutes, seconds)
    } else {
        String.format("%02d:%02d", minutes, seconds)
    }
}