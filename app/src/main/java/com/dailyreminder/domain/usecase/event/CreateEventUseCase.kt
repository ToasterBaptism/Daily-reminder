package com.dailyreminder.domain.usecase.event

import com.dailyreminder.data.local.repository.PlannerRepository
import com.dailyreminder.data.model.Event
import com.dailyreminder.data.util.DateUtils
import com.dailyreminder.domain.util.Result
import javax.inject.Inject

class CreateEventUseCase @Inject constructor(
    private val repository: PlannerRepository
) {
    
    suspend operator fun invoke(event: Event): Result<Long> {
        return try {
            val now = DateUtils.now()
            val eventToCreate = event.copy(
                createdAt = now,
                updatedAt = now
            )
            
            val eventId = repository.insertEvent(eventToCreate)
            Result.Success(eventId)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}