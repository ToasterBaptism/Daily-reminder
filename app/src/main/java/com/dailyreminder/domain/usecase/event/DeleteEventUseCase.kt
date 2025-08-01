package com.dailyreminder.domain.usecase.event

import com.dailyreminder.data.local.repository.PlannerRepository
import com.dailyreminder.data.model.Event
import com.dailyreminder.domain.util.Result
import javax.inject.Inject

class DeleteEventUseCase @Inject constructor(
    private val repository: PlannerRepository
) {
    
    suspend operator fun invoke(event: Event): Result<Unit> {
        return try {
            repository.deleteEvent(event)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
    
    suspend fun deleteById(id: Long): Result<Unit> {
        return try {
            repository.deleteEventById(id)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}