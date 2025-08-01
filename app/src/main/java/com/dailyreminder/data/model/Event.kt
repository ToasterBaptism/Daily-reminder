package com.dailyreminder.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.datetime.LocalDateTime

@Parcelize
data class Event(
    val id: Long = 0,
    val title: String,
    val description: String = "",
    val startDateTime: LocalDateTime,
    val endDateTime: LocalDateTime? = null,
    val category: EventCategory = EventCategory.PERSONAL,
    val priority: Priority = Priority.MEDIUM,
    val recurrence: Recurrence = Recurrence.NONE,
    val isCompleted: Boolean = false,
    val notificationEnabled: Boolean = true,
    val notificationMinutesBefore: Int = 15,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
) : Parcelable

enum class EventCategory {
    PERSONAL,
    WORK,
    MEDICAL,
    SOCIAL,
    RECREATION,
    OTHER
}

enum class Priority {
    LOW,
    MEDIUM,
    HIGH,
    URGENT
}

enum class Recurrence {
    NONE,
    DAILY,
    WEEKLY,
    MONTHLY,
    YEARLY
}