package com.dailyreminder.domain.usecase.task

import com.dailyreminder.data.local.repository.PlannerRepository
import com.dailyreminder.data.model.Task
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTasksUseCase @Inject constructor(
    private val repository: PlannerRepository
) {
    operator fun invoke(): Flow<List<Task>> {
        return repository.getAllTasks()
    }
    
    fun getCompletedTasks(): Flow<List<Task>> {
        return repository.getTasksByCompletionStatus(true)
    }
    
    fun getPendingTasks(): Flow<List<Task>> {
        return repository.getTasksByCompletionStatus(false)
    }
}