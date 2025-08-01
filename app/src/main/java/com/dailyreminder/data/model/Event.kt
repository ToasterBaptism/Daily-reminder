package com.dailyreminder.data.model

import kotlinx.datetime.LocalDateTime

data class Event(
    val id: Long = 0,
    val title: String,
    val description: String = "",
    val startDateTime: LocalDateTime,
    val endDateTime: LocalDateTime,
    val category: EventCategory = EventCategory.PERSONAL,
    val priority: Priority = Priority.MEDIUM,
    val isAllDay: Boolean = false,
    val recurrence: RecurrenceType = RecurrenceType.NONE,
    val reminderMinutes: Int = 15,
    val location: String = "",
    val isCompleted: Boolean = false,
    val createdAt: LocalDateTime = LocalDateTime.parse("2024-01-01T00:00:00"),
    val updatedAt: LocalDateTime = LocalDateTime.parse("2024-01-01T00:00:00")
)

enum class EventCategory {
    PERSONAL,
    WORK,
    HEALTH,
    SOCIAL,
    FAMILY,
    EDUCATION,
    TRAVEL,
    OTHER
}

enum class Priority {
    LOW,
    MEDIUM,
    HIGH,
    URGENT
}

enum class RecurrenceType {
    NONE,
    DAILY,
    WEEKLY,
    MONTHLY,
    YEARLY
}