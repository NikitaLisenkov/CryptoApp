package com.example.cryptoapp.utils

import java.sql.Date
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

fun convertTimestampToTime(timestamp: Int?): String {
    if (timestamp == null) return ""
    val stamp = Timestamp((timestamp * 1000).toLong()) //Timestamp принимает милисекунды, а с сервера приходят секунды
    val date = Date(stamp.time)
    val pattern = "HH:mm:ss"
    val sdf = SimpleDateFormat(pattern, Locale.getDefault())
    sdf.timeZone = TimeZone.getDefault()
    return sdf.format(date)
}