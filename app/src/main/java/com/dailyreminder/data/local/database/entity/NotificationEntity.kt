package com.dailyreminder.data.local.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.dailyreminder.data.model.Notification
import com.dailyreminder.data.model.NotificationType
import kotlinx.datetime.LocalDateTime

@Entity(tableName = "notifications")
data class NotificationEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val message: String,
    val type: String,
    val relatedItemId: Long,
    val scheduledDateTime: String, // ISO string format
    val isDelivered: Boolean,
    val isDismissed: Boolean,
    val soundEnabled: Boolean,
    val vibrationEnabled: Boolean,
    val snoozeCount: Int,
    val maxSnoozeCount: Int,
    val snoozeIntervalMinutes: Int,
    val createdAt: String, // ISO string format
    val deliveredAt: String? // ISO string format, nullable
)

fun NotificationEntity.toNotification(): Notification {
    return Notification(
        id = id,
        title = title,
        message = message,
        type = NotificationType.valueOf(type),
        relatedItemId = relatedItemId,
        scheduledDateTime = LocalDateTime.parse(scheduledDateTime),
        isDelivered = isDelivered,
        isDismissed = isDismissed,
        soundEnabled = soundEnabled,
        vibrationEnabled = vibrationEnabled,
        snoozeCount = snoozeCount,
        maxSnoozeCount = maxSnoozeCount,
        snoozeIntervalMinutes = snoozeIntervalMinutes,
        createdAt = LocalDateTime.parse(createdAt),
        deliveredAt = deliveredAt?.let { LocalDateTime.parse(it) }
    )
}

fun Notification.toEntity(): NotificationEntity {
    return NotificationEntity(
        id = id,
        title = title,
        message = message,
        type = type.name,
        relatedItemId = relatedItemId,
        scheduledDateTime = scheduledDateTime.toString(),
        isDelivered = isDelivered,
        isDismissed = isDismissed,
        soundEnabled = soundEnabled,
        vibrationEnabled = vibrationEnabled,
        snoozeCount = snoozeCount,
        maxSnoozeCount = maxSnoozeCount,
        snoozeIntervalMinutes = snoozeIntervalMinutes,
        createdAt = createdAt.toString(),
        deliveredAt = deliveredAt?.toString()
    )
}