package com.dailyreminder.data.model

import kotlinx.datetime.LocalDateTime

data class Notification(
    val id: Long = 0,
    val title: String,
    val message: String,
    val type: NotificationType,
    val relatedItemId: Long,
    val scheduledDateTime: LocalDateTime,
    val isDelivered: Boolean = false,
    val isDismissed: Boolean = false,
    val soundEnabled: Boolean = true,
    val vibrationEnabled: Boolean = true,
    val snoozeCount: Int = 0,
    val maxSnoozeCount: Int = 3,
    val snoozeIntervalMinutes: Int = 5,
    val createdAt: LocalDateTime = LocalDateTime.parse("2024-01-01T00:00:00"),
    val deliveredAt: LocalDateTime? = null
)

enum class NotificationType {
    MEAL_REMINDER,
    TASK_REMINDER,
    EVENT_REMINDER,
    BACKUP_REMINDER,
    GROCERY_LIST_REMINDER,
    MEDICATION_REMINDER
}