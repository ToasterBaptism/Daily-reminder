package com.dailyreminder.domain.usecase.task

import com.dailyreminder.data.local.repository.PlannerRepository
import com.dailyreminder.data.model.Task
import com.dailyreminder.data.util.DateUtils
import com.dailyreminder.domain.util.Result
import javax.inject.Inject

class CompleteTaskUseCase @Inject constructor(
    private val repository: PlannerRepository
) {
    
    suspend operator fun invoke(taskId: Long): Result<Unit> {
        return try {
            val task = repository.getTaskById(taskId)
            if (task != null) {
                val now = DateUtils.now()
                val completedTask = task.copy(
                    isCompleted = true,
                    completedAt = now,
                    progress = 100,
                    updatedAt = now
                )
                repository.updateTask(completedTask)
                Result.Success(Unit)
            } else {
                Result.Error(IllegalArgumentException("Task not found"))
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
    
    suspend fun uncompleteTask(taskId: Long): Result<Unit> {
        return try {
            val task = repository.getTaskById(taskId)
            if (task != null) {
                val now = DateUtils.now()
                val uncompletedTask = task.copy(
                    isCompleted = false,
                    completedAt = null,
                    progress = 0,
                    updatedAt = now
                )
                repository.updateTask(uncompletedTask)
                Result.Success(Unit)
            } else {
                Result.Error(IllegalArgumentException("Task not found"))
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
    
    suspend fun updateTaskProgress(taskId: Long, progress: Int): Result<Unit> {
        return try {
            val task = repository.getTaskById(taskId)
            if (task != null) {
                val now = DateUtils.now()
                val updatedTask = task.copy(
                    progress = progress.coerceIn(0, 100),
                    isCompleted = progress >= 100,
                    completedAt = if (progress >= 100) now else null,
                    updatedAt = now
                )
                repository.updateTask(updatedTask)
                Result.Success(Unit)
            } else {
                Result.Error(IllegalArgumentException("Task not found"))
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}