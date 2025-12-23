package com.example.baitap10.util

import java.text.SimpleDateFormat
import java.util.*

object DateUtils {
    @JvmStatic
    fun formatDate(timestamp: Long?): String {
        if (timestamp == null) return ""
        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        return sdf.format(Date(timestamp))
    }
}
