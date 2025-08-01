package com.dailyreminder.presentation.service

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.dailyreminder.R
import com.dailyreminder.MainActivity
import com.dailyreminder.data.model.*
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class NotificationService : Service() {
    
    companion object {
        const val CHANNEL_ID_EVENTS = "events_channel"
        const val CHANNEL_ID_MEALS = "meals_channel"
        const val CHANNEL_ID_TASKS = "tasks_channel"
        const val CHANNEL_ID_REMINDERS = "reminders_channel"
        
        const val NOTIFICATION_ID_EVENT = 1000
        const val NOTIFICATION_ID_MEAL = 2000
        const val NOTIFICATION_ID_TASK = 3000
        const val NOTIFICATION_ID_REMINDER = 4000
        
        const val EXTRA_NOTIFICATION_TYPE = "notification_type"
        const val EXTRA_ITEM_ID = "item_id"
        const val EXTRA_TITLE = "title"
        const val EXTRA_MESSAGE = "message"
        
        fun createNotificationChannels(context: Context) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                
                // Events channel
                val eventsChannel = NotificationChannel(
                    CHANNEL_ID_EVENTS,
                    "Events",
                    NotificationManager.IMPORTANCE_HIGH
                ).apply {
                    description = "Notifications for scheduled events"
                    enableLights(true)
                    enableVibration(true)
                    setShowBadge(true)
                }
                
                // Meals channel
                val mealsChannel = NotificationChannel(
                    CHANNEL_ID_MEALS,
                    "Meals",
                    NotificationManager.IMPORTANCE_HIGH
                ).apply {
                    description = "Notifications for meal planning"
                    enableLights(true)
                    enableVibration(true)
                    setShowBadge(true)
                }
                
                // Tasks channel
                val tasksChannel = NotificationChannel(
                    CHANNEL_ID_TASKS,
                    "Tasks",
                    NotificationManager.IMPORTANCE_DEFAULT
                ).apply {
                    description = "Notifications for task reminders"
                    enableLights(true)
                    enableVibration(true)
                    setShowBadge(true)
                }
                
                // General reminders channel
                val remindersChannel = NotificationChannel(
                    CHANNEL_ID_REMINDERS,
                    "Reminders",
                    NotificationManager.IMPORTANCE_DEFAULT
                ).apply {
                    description = "General reminder notifications"
                    enableLights(true)
                    enableVibration(false)
                    setShowBadge(false)
                }
                
                notificationManager.createNotificationChannels(listOf(
                    eventsChannel,
                    mealsChannel,
                    tasksChannel,
                    remindersChannel
                ))
            }
        }
    }
    
    override fun onBind(intent: Intent?) = null
    
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let { handleNotificationIntent(it) }
        return START_NOT_STICKY
    }
    
    private fun handleNotificationIntent(intent: Intent) {
        val notificationType = intent.getStringExtra(EXTRA_NOTIFICATION_TYPE) ?: return
        val itemId = intent.getLongExtra(EXTRA_ITEM_ID, 0L)
        val title = intent.getStringExtra(EXTRA_TITLE) ?: ""
        val message = intent.getStringExtra(EXTRA_MESSAGE) ?: ""
        
        when (notificationType) {
            "EVENT" -> showEventNotification(itemId, title, message)
            "MEAL" -> showMealNotification(itemId, title, message)
            "TASK" -> showTaskNotification(itemId, title, message)
            "REMINDER" -> showReminderNotification(title, message)
        }
        
        stopSelf(startId)
    }
    
    private fun showEventNotification(eventId: Long, title: String, message: String) {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("navigate_to", "event_detail/$eventId")
        }
        
        val pendingIntent = PendingIntent.getActivity(
            this,
            eventId.toInt(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        
        val notification = NotificationCompat.Builder(this, CHANNEL_ID_EVENTS)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(title)
            .setContentText(message)
            .setStyle(NotificationCompat.BigTextStyle().bigText(message))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_EVENT)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .addAction(
                R.drawable.ic_notification,
                "View",
                pendingIntent
            )
            .build()
        
        NotificationManagerCompat.from(this).notify(
            NOTIFICATION_ID_EVENT + eventId.toInt(),
            notification
        )
    }
    
    private fun showMealNotification(mealId: Long, title: String, message: String) {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("navigate_to", "meal_detail/$mealId")
        }
        
        val pendingIntent = PendingIntent.getActivity(
            this,
            mealId.toInt(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        
        val notification = NotificationCompat.Builder(this, CHANNEL_ID_MEALS)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(title)
            .setContentText(message)
            .setStyle(NotificationCompat.BigTextStyle().bigText(message))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_REMINDER)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .addAction(
                R.drawable.ic_notification,
                "View",
                pendingIntent
            )
            .build()
        
        NotificationManagerCompat.from(this).notify(
            NOTIFICATION_ID_MEAL + mealId.toInt(),
            notification
        )
    }
    
    private fun showTaskNotification(taskId: Long, title: String, message: String) {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("navigate_to", "task_detail/$taskId")
        }
        
        val pendingIntent = PendingIntent.getActivity(
            this,
            taskId.toInt(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        
        val notification = NotificationCompat.Builder(this, CHANNEL_ID_TASKS)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(title)
            .setContentText(message)
            .setStyle(NotificationCompat.BigTextStyle().bigText(message))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setCategory(NotificationCompat.CATEGORY_REMINDER)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .addAction(
                R.drawable.ic_notification,
                "View",
                pendingIntent
            )
            .build()
        
        NotificationManagerCompat.from(this).notify(
            NOTIFICATION_ID_TASK + taskId.toInt(),
            notification
        )
    }
    
    private fun showReminderNotification(title: String, message: String) {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        
        val notification = NotificationCompat.Builder(this, CHANNEL_ID_REMINDERS)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(title)
            .setContentText(message)
            .setStyle(NotificationCompat.BigTextStyle().bigText(message))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setCategory(NotificationCompat.CATEGORY_REMINDER)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()
        
        NotificationManagerCompat.from(this).notify(
            NOTIFICATION_ID_REMINDER,
            notification
        )
    }
}