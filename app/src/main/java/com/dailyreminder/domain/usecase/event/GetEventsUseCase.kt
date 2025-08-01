package com.dailyreminder.domain.usecase.event

import com.dailyreminder.data.local.repository.PlannerRepository
import com.dailyreminder.data.model.Event
import com.dailyreminder.data.model.EventCategory
import com.dailyreminder.domain.util.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.datetime.LocalDateTime
import javax.inject.Inject

class GetEventsUseCase @Inject constructor(
    private val repository: PlannerRepository
) {
    
    fun getAllEvents(): Flow<Result<List<Event>>> {
        return repository.getAllEvents()
            .map { Result.Success(it) as Result<List<Event>> }
            .catch { emit(Result.Error(it)) }
    }
    
    fun getEventsByDate(date: LocalDateTime): Flow<Result<List<Event>>> {
        return repository.getEventsByDate(date)
            .map { Result.Success(it) as Result<List<Event>> }
            .catch { emit(Result.Error(it)) }
    }
    
    fun getEventsBetweenDates(startDate: LocalDateTime, endDate: LocalDateTime): Flow<Result<List<Event>>> {
        return repository.getEventsBetweenDates(startDate, endDate)
            .map { Result.Success(it) as Result<List<Event>> }
            .catch { emit(Result.Error(it)) }
    }
    
    fun getEventsByCategory(category: EventCategory): Flow<Result<List<Event>>> {
        return repository.getEventsByCategory(category)
            .map { Result.Success(it) as Result<List<Event>> }
            .catch { emit(Result.Error(it)) }
    }
    
    fun getUpcomingEventsWithNotifications(currentTime: LocalDateTime): Flow<Result<List<Event>>> {
        return repository.getUpcomingEventsWithNotifications(currentTime)
            .map { Result.Success(it) as Result<List<Event>> }
            .catch { emit(Result.Error(it)) }
    }
    
    fun searchEvents(query: String): Flow<Result<List<Event>>> {
        return repository.searchEvents(query)
            .map { Result.Success(it) as Result<List<Event>> }
            .catch { emit(Result.Error(it)) }
    }
    
    suspend fun getEventById(id: Long): Result<Event?> {
        return try {
            val event = repository.getEventById(id)
            Result.Success(event)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}