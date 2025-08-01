package com.dailyreminder.data.model

import kotlinx.datetime.LocalDateTime

data class Task(
    val id: Long = 0,
    val title: String,
    val description: String = "",
    val category: TaskCategory,
    val priority: Priority = Priority.MEDIUM,
    val dueDateTime: LocalDateTime?,
    val estimatedDurationMinutes: Int = 30,
    val recurrence: RecurrenceType = RecurrenceType.NONE,
    val reminderMinutes: Int = 15,
    val isCompleted: Boolean = false,
    val completedAt: LocalDateTime? = null,
    val subtasks: List<Subtask> = emptyList(),
    val tags: List<String> = emptyList(),
    val createdAt: LocalDateTime = LocalDateTime.parse("2024-01-01T00:00:00"),
    val updatedAt: LocalDateTime = LocalDateTime.parse("2024-01-01T00:00:00")
)

data class Subtask(
    val id: Long = 0,
    val title: String,
    val isCompleted: Boolean = false,
    val createdAt: LocalDateTime = LocalDateTime.parse("2024-01-01T00:00:00")
)

enum class TaskCategory {
    COOKING_MEAL_PREP,
    CLEANING_HOUSEHOLD,
    GROCERY_SHOPPING,
    MEDICAL_APPOINTMENTS,
    PERSONAL_CARE,
    WORK_PROFESSIONAL,
    RECREATION_SOCIAL,
    EDUCATION_LEARNING,
    FINANCE_BILLS,
    MAINTENANCE_REPAIRS,
    OTHER
}