package com.dailyreminder.domain.usecase.task

import com.dailyreminder.data.local.repository.PlannerRepository
import com.dailyreminder.data.model.Task
import com.dailyreminder.data.util.DateUtils
import com.dailyreminder.domain.util.Result
import javax.inject.Inject

class CreateTaskUseCase @Inject constructor(
    private val repository: PlannerRepository
) {
    
    suspend operator fun invoke(task: Task): Result<Long> {
        return try {
            val now = DateUtils.now()
            val taskToCreate = task.copy(
                createdAt = now,
                updatedAt = now
            )
            
            val taskId = repository.insertTask(taskToCreate)
            Result.Success(taskId)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}