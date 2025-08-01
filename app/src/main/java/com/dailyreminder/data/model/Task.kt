package com.dailyreminder.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.datetime.LocalDateTime

@Parcelize
data class Task(
    val id: Long = 0,
    val title: String,
    val description: String = "",
    val category: TaskCategory = TaskCategory.PERSONAL,
    val priority: Priority = Priority.MEDIUM,
    val dueDateTime: LocalDateTime? = null,
    val estimatedDurationMinutes: Int = 30,
    val isCompleted: Boolean = false,
    val completedAt: LocalDateTime? = null,
    val recurrence: Recurrence = Recurrence.NONE,
    val subtasks: List<Subtask> = emptyList(),
    val tags: List<String> = emptyList(),
    val notificationEnabled: Boolean = true,
    val notificationMinutesBefore: Int = 15,
    val progress: Int = 0, // 0-100
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
) : Parcelable

@Parcelize
data class Subtask(
    val id: Long = 0,
    val title: String,
    val isCompleted: Boolean = false,
    val completedAt: LocalDateTime? = null
) : Parcelable

enum class TaskCategory {
    COOKING_MEAL_PREP,
    CLEANING_HOUSEHOLD,
    GROCERY_SHOPPING,
    MEDICAL_APPOINTMENTS,
    PERSONAL_CARE,
    WORK_PROFESSIONAL,
    RECREATION_SOCIAL,
    OTHER
}