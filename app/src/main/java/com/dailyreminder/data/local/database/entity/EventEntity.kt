package com.dailyreminder.data.local.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.dailyreminder.data.local.database.converter.DateTimeConverter
import com.dailyreminder.data.local.database.converter.EnumConverter
import com.dailyreminder.data.model.EventCategory
import com.dailyreminder.data.model.Priority
import com.dailyreminder.data.model.Recurrence
import kotlinx.datetime.LocalDateTime

@Entity(tableName = "events")
@TypeConverters(DateTimeConverter::class, EnumConverter::class)
data class EventEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val description: String,
    val startDateTime: LocalDateTime,
    val endDateTime: LocalDateTime?,
    val category: EventCategory,
    val priority: Priority,
    val recurrence: Recurrence,
    val isCompleted: Boolean,
    val notificationEnabled: Boolean,
    val notificationMinutesBefore: Int,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)