package com.dailyreminder.application

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class DailyReminderApplication : Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    override fun onCreate() {
        super.onCreate()
        createNotificationChannels()
    }

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()

    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            // Meal reminder channel
            val mealChannel = NotificationChannel(
                MEAL_CHANNEL_ID,
                "Meal Reminders",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Notifications for meal planning and cooking reminders"
                enableVibration(true)
                setShowBadge(true)
            }

            // Task reminder channel
            val taskChannel = NotificationChannel(
                TASK_CHANNEL_ID,
                "Task Reminders",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Notifications for task and appointment reminders"
                enableVibration(true)
                setShowBadge(true)
            }

            // Event reminder channel
            val eventChannel = NotificationChannel(
                EVENT_CHANNEL_ID,
                "Event Reminders",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Notifications for calendar events and appointments"
                enableVibration(true)
                setShowBadge(true)
            }

            // Backup reminder channel
            val backupChannel = NotificationChannel(
                BACKUP_CHANNEL_ID,
                "Backup Reminders",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Notifications for data backup reminders"
                enableVibration(false)
                setShowBadge(false)
            }

            notificationManager.createNotificationChannels(
                listOf(mealChannel, taskChannel, eventChannel, backupChannel)
            )
        }
    }

    companion object {
        const val MEAL_CHANNEL_ID = "meal_reminders"
        const val TASK_CHANNEL_ID = "task_reminders"
        const val EVENT_CHANNEL_ID = "event_reminders"
        const val BACKUP_CHANNEL_ID = "backup_reminders"
    }
}