package com.dailyreminder.domain.usecase.task

import com.dailyreminder.data.local.repository.PlannerRepository
import com.dailyreminder.data.model.Task
import com.dailyreminder.data.model.TaskCategory
import com.dailyreminder.domain.util.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.datetime.LocalDateTime
import javax.inject.Inject

class GetTasksUseCase @Inject constructor(
    private val repository: PlannerRepository
) {
    
    fun getAllTasks(): Flow<Result<List<Task>>> {
        return repository.getAllTasks()
            .map { Result.Success(it) as Result<List<Task>> }
            .catch { emit(Result.Error(it)) }
    }
    
    fun getTasksByDate(date: LocalDateTime): Flow<Result<List<Task>>> {
        return repository.getTasksByDate(date)
            .map { Result.Success(it) as Result<List<Task>> }
            .catch { emit(Result.Error(it)) }
    }
    
    fun getTasksByCategory(category: TaskCategory): Flow<Result<List<Task>>> {
        return repository.getTasksByCategory(category)
            .map { Result.Success(it) as Result<List<Task>> }
            .catch { emit(Result.Error(it)) }
    }
    
    fun getTasksByCompletionStatus(isCompleted: Boolean): Flow<Result<List<Task>>> {
        return repository.getTasksByCompletionStatus(isCompleted)
            .map { Result.Success(it) as Result<List<Task>> }
            .catch { emit(Result.Error(it)) }
    }
    
    fun getOverdueTasks(currentTime: LocalDateTime): Flow<Result<List<Task>>> {
        return repository.getOverdueTasks(currentTime)
            .map { Result.Success(it) as Result<List<Task>> }
            .catch { emit(Result.Error(it)) }
    }
    
    fun searchTasks(query: String): Flow<Result<List<Task>>> {
        return repository.searchTasks(query)
            .map { Result.Success(it) as Result<List<Task>> }
            .catch { emit(Result.Error(it)) }
    }
    
    suspend fun getTaskById(id: Long): Result<Task?> {
        return try {
            val task = repository.getTaskById(id)
            Result.Success(task)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}