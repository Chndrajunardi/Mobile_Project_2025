package com.example.todolistapp.utils

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

object DateUtils {
    private val dateFormat = SimpleDateFormat("dd MMMM yyyy HH:mm", Locale.getDefault())

    fun formatDateTime(calendar: Calendar): String {
        return dateFormat.format(calendar.time)
    }

    fun parseDateTime(dateString: String): Calendar? {
        return try {
            val calendar = Calendar.getInstance()
            calendar.time = dateFormat.parse(dateString) ?: return null
            calendar
        } catch (e: Exception) {
            null
        }
    }
} 