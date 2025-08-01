package com.dailyreminder.data.local.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.dailyreminder.data.local.database.converter.DateTimeConverter
import com.dailyreminder.data.local.database.converter.EnumConverter
import com.dailyreminder.data.local.database.converter.TaskConverter
import com.dailyreminder.data.model.*
import kotlinx.datetime.LocalDateTime

@Entity(tableName = "tasks")
@TypeConverters(DateTimeConverter::class, EnumConverter::class, TaskConverter::class)
data class TaskEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val description: String,
    val category: TaskCategory,
    val priority: Priority,
    val dueDateTime: LocalDateTime?,
    val estimatedDurationMinutes: Int,
    val isCompleted: Boolean,
    val completedAt: LocalDateTime?,
    val recurrence: Recurrence,
    val subtasks: List<Subtask>,
    val tags: List<String>,
    val notificationEnabled: Boolean,
    val notificationMinutesBefore: Int,
    val progress: Int,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)