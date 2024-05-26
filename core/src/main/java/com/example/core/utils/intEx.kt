package com.example.core.utils

fun Int.toHoursAndMinutes(): String {
    val hours = this / 60
    val minutes = this % 60
    return "${hours}h ${minutes}min"
}
