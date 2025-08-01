package com.dailyreminder.domain.usecase.task

import com.dailyreminder.data.local.repository.PlannerRepository
import com.dailyreminder.data.model.Task
import com.dailyreminder.data.util.DateUtils
import com.dailyreminder.domain.util.Result
import javax.inject.Inject

class CompleteTaskUseCase @Inject constructor(
    private val repository: PlannerRepository
) {
    suspend operator fun invoke(task: Task, isCompleted: Boolean): Result<Unit> {
        val now = DateUtils.getCurrentDateTime()
        val updatedTask = task.copy(
            isCompleted = isCompleted,
            completedAt = if (isCompleted) now else null,
            updatedAt = now
        )
        
        return repository.updateTask(updatedTask)
    }
}