package com.dailyreminder.data.local.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.dailyreminder.data.model.Priority
import com.dailyreminder.data.model.RecurrenceType
import com.dailyreminder.data.model.Subtask
import com.dailyreminder.data.model.Task
import com.dailyreminder.data.model.TaskCategory
import kotlinx.datetime.LocalDateTime

@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val description: String,
    val category: String,
    val priority: String,
    val dueDateTime: String?, // ISO string format, nullable
    val estimatedDurationMinutes: Int,
    val recurrence: String,
    val reminderMinutes: Int,
    val isCompleted: Boolean,
    val completedAt: String?, // ISO string format, nullable
    val subtasks: String, // JSON string
    val tags: String, // JSON string
    val createdAt: String, // ISO string format
    val updatedAt: String // ISO string format
)

fun TaskEntity.toTask(): Task {
    return Task(
        id = id,
        title = title,
        description = description,
        category = TaskCategory.valueOf(category),
        priority = Priority.valueOf(priority),
        dueDateTime = dueDateTime?.let { LocalDateTime.parse(it) },
        estimatedDurationMinutes = estimatedDurationMinutes,
        recurrence = RecurrenceType.valueOf(recurrence),
        reminderMinutes = reminderMinutes,
        isCompleted = isCompleted,
        completedAt = completedAt?.let { LocalDateTime.parse(it) },
        subtasks = parseSubtasks(subtasks),
        tags = parseJsonStringList(tags),
        createdAt = LocalDateTime.parse(createdAt),
        updatedAt = LocalDateTime.parse(updatedAt)
    )
}

fun Task.toEntity(): TaskEntity {
    return TaskEntity(
        id = id,
        title = title,
        description = description,
        category = category.name,
        priority = priority.name,
        dueDateTime = dueDateTime?.toString(),
        estimatedDurationMinutes = estimatedDurationMinutes,
        recurrence = recurrence.name,
        reminderMinutes = reminderMinutes,
        isCompleted = isCompleted,
        completedAt = completedAt?.toString(),
        subtasks = subtasks.subtasksToJsonString(),
        tags = tags.toJsonString(),
        createdAt = createdAt.toString(),
        updatedAt = updatedAt.toString()
    )
}

private fun parseSubtasks(json: String): List<Subtask> {
    if (json.isBlank()) return emptyList()
    
    // Simple parsing - in production, use proper JSON library
    return json.split("|").mapNotNull { subtaskStr ->
        val parts = subtaskStr.split(":")
        if (parts.size >= 2) {
            Subtask(
                id = parts[0].toLongOrNull() ?: 0,
                title = parts[1],
                isCompleted = parts.getOrNull(2)?.toBoolean() ?: false
            )
        } else null
    }
}

private fun List<Subtask>.subtasksToJsonString(): String {
    return this.joinToString("|") { "${it.id}:${it.title}:${it.isCompleted}" }
}

private fun parseJsonStringList(json: String): List<String> {
    return if (json.isBlank()) emptyList() else json.split(",").map { it.trim() }
}

private fun List<String>.toJsonString(): String {
    return this.joinToString(",")
}