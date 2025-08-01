package com.dailyreminder.presentation.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AlarmReceiver : BroadcastReceiver() {
    
    override fun onReceive(context: Context, intent: Intent) {
        val notificationType = intent.getStringExtra(NotificationService.EXTRA_NOTIFICATION_TYPE)
        val itemId = intent.getLongExtra(NotificationService.EXTRA_ITEM_ID, 0L)
        val title = intent.getStringExtra(NotificationService.EXTRA_TITLE) ?: ""
        val message = intent.getStringExtra(NotificationService.EXTRA_MESSAGE) ?: ""
        
        // Create and start the notification service
        val serviceIntent = Intent(context, NotificationService::class.java).apply {
            putExtra(NotificationService.EXTRA_NOTIFICATION_TYPE, notificationType)
            putExtra(NotificationService.EXTRA_ITEM_ID, itemId)
            putExtra(NotificationService.EXTRA_TITLE, title)
            putExtra(NotificationService.EXTRA_MESSAGE, message)
        }
        
        context.startService(serviceIntent)
    }
}