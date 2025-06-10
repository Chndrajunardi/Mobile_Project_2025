package com.example.todolistapp.utils

import android.content.Context
import android.widget.Toast

object ToastUtils {
    private var lastToast: Toast? = null
    private var lastMessage: String? = null
    private var lastShowTime: Long = 0
    private const val TOAST_INTERVAL = 2000 // 2 seconds

    fun showToast(context: Context, message: String) {
        val currentTime = System.currentTimeMillis()
        
        // Check if the same message was shown recently
        if (message == lastMessage && currentTime - lastShowTime < TOAST_INTERVAL) {
            return
        }

        // Cancel previous toast if it exists
        lastToast?.cancel()

        // Create and show new toast
        lastToast = Toast.makeText(context, message, Toast.LENGTH_SHORT).apply {
            show()
        }
        
        lastMessage = message
        lastShowTime = currentTime
    }
} 