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
        val now = DateUtils.getCurrentDateTime()
        val taskWithTimestamp = task.copy(
            createdAt = now,
            updatedAt = now
        )
        
        return repository.insertTask(taskWithTimestamp)
    }
}