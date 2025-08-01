package com.dailyreminder.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.datetime.LocalDateTime

@Parcelize
data class Notification(
    val id: Long = 0,
    val title: String,
    val message: String,
    val type: NotificationType,
    val relatedItemId: Long, // ID of related event, meal, or task
    val scheduledDateTime: LocalDateTime,
    val isDelivered: Boolean = false,
    val deliveredAt: LocalDateTime? = null,
    val isDismissed: Boolean = false,
    val dismissedAt: LocalDateTime? = null,
    val isSnoozed: Boolean = false,
    val snoozeUntil: LocalDateTime? = null,
    val soundEnabled: Boolean = true,
    val vibrationEnabled: Boolean = true,
    val ledEnabled: Boolean = true,
    val priority: NotificationPriority = NotificationPriority.DEFAULT,
    val actions: List<NotificationAction> = emptyList(),
    val createdAt: LocalDateTime
) : Parcelable

@Parcelize
data class NotificationAction(
    val id: String,
    val title: String,
    val actionType: NotificationActionType
) : Parcelable

enum class NotificationType {
    EVENT_REMINDER,
    MEAL_REMINDER,
    TASK_REMINDER,
    BACKUP_REMINDER,
    SYSTEM_NOTIFICATION
}

enum class NotificationPriority {
    MIN,
    LOW,
    DEFAULT,
    HIGH,
    MAX
}

enum class NotificationActionType {
    DISMISS,
    SNOOZE_5_MIN,
    SNOOZE_15_MIN,
    SNOOZE_30_MIN,
    SNOOZE_1_HOUR,
    MARK_COMPLETE,
    VIEW_DETAILS,
    OPEN_APP
}