package com.example.prayerwidget.domain.usecase

fun transformTime(prayerTime: String): String {
    val time = prayerTime.dropLast(6)
    val (hourIn24, minutes) = time.split(":")
    val hour = hourIn24.toInt()
    val amPm = if (hour > 12)
        "PM"
    else
        "AM"
    val hourIn12 = if (hour > 12)
        "0${hour - 12}"
    else
        hourIn24
    return "$hourIn12:$minutes $amPm"
}