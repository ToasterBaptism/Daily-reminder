package com.dailyreminder.data.local.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.dailyreminder.data.local.database.converter.DateTimeConverter
import com.dailyreminder.data.local.database.converter.EnumConverter
import com.dailyreminder.data.local.database.converter.NotificationConverter
import com.dailyreminder.data.model.*
import kotlinx.datetime.LocalDateTime

@Entity(tableName = "notifications")
@TypeConverters(DateTimeConverter::class, EnumConverter::class, NotificationConverter::class)
data class NotificationEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val message: String,
    val type: NotificationType,
    val relatedItemId: Long,
    val scheduledDateTime: LocalDateTime,
    val isDelivered: Boolean,
    val deliveredAt: LocalDateTime?,
    val isDismissed: Boolean,
    val dismissedAt: LocalDateTime?,
    val isSnoozed: Boolean,
    val snoozeUntil: LocalDateTime?,
    val soundEnabled: Boolean,
    val vibrationEnabled: Boolean,
    val ledEnabled: Boolean,
    val priority: NotificationPriority,
    val actions: List<NotificationAction>,
    val createdAt: LocalDateTime
)