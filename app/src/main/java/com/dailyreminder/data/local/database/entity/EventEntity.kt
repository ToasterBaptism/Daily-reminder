package com.dailyreminder.data.local.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.dailyreminder.data.model.Event
import com.dailyreminder.data.model.EventCategory
import com.dailyreminder.data.model.Priority
import com.dailyreminder.data.model.RecurrenceType
import kotlinx.datetime.LocalDateTime

@Entity(tableName = "events")
data class EventEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val description: String,
    val startDateTime: String, // ISO string format
    val endDateTime: String, // ISO string format
    val category: String,
    val priority: String,
    val isAllDay: Boolean,
    val recurrence: String,
    val reminderMinutes: Int,
    val location: String,
    val isCompleted: Boolean,
    val createdAt: String, // ISO string format
    val updatedAt: String // ISO string format
)

fun EventEntity.toEvent(): Event {
    return Event(
        id = id,
        title = title,
        description = description,
        startDateTime = LocalDateTime.parse(startDateTime),
        endDateTime = LocalDateTime.parse(endDateTime),
        category = EventCategory.valueOf(category),
        priority = Priority.valueOf(priority),
        isAllDay = isAllDay,
        recurrence = RecurrenceType.valueOf(recurrence),
        reminderMinutes = reminderMinutes,
        location = location,
        isCompleted = isCompleted,
        createdAt = LocalDateTime.parse(createdAt),
        updatedAt = LocalDateTime.parse(updatedAt)
    )
}

fun Event.toEntity(): EventEntity {
    return EventEntity(
        id = id,
        title = title,
        description = description,
        startDateTime = startDateTime.toString(),
        endDateTime = endDateTime.toString(),
        category = category.name,
        priority = priority.name,
        isAllDay = isAllDay,
        recurrence = recurrence.name,
        reminderMinutes = reminderMinutes,
        location = location,
        isCompleted = isCompleted,
        createdAt = createdAt.toString(),
        updatedAt = updatedAt.toString()
    )
}