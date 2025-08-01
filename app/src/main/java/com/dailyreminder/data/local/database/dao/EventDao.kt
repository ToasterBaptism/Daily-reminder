package com.dailyreminder.data.local.database.dao

import androidx.room.*
import com.dailyreminder.data.local.database.entity.EventEntity
import com.dailyreminder.data.model.EventCategory
import com.dailyreminder.data.model.Priority
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDateTime

@Dao
interface EventDao {
    
    @Query("SELECT * FROM events ORDER BY startDateTime ASC")
    fun getAllEvents(): Flow<List<EventEntity>>
    
    @Query("SELECT * FROM events WHERE id = :id")
    suspend fun getEventById(id: Long): EventEntity?
    
    @Query("SELECT * FROM events WHERE DATE(startDateTime) = DATE(:date) ORDER BY startDateTime ASC")
    fun getEventsByDate(date: LocalDateTime): Flow<List<EventEntity>>
    
    @Query("SELECT * FROM events WHERE startDateTime BETWEEN :startDate AND :endDate ORDER BY startDateTime ASC")
    fun getEventsBetweenDates(startDate: LocalDateTime, endDate: LocalDateTime): Flow<List<EventEntity>>
    
    @Query("SELECT * FROM events WHERE category = :category ORDER BY startDateTime ASC")
    fun getEventsByCategory(category: EventCategory): Flow<List<EventEntity>>
    
    @Query("SELECT * FROM events WHERE priority = :priority ORDER BY startDateTime ASC")
    fun getEventsByPriority(priority: Priority): Flow<List<EventEntity>>
    
    @Query("SELECT * FROM events WHERE isCompleted = :isCompleted ORDER BY startDateTime ASC")
    fun getEventsByCompletionStatus(isCompleted: Boolean): Flow<List<EventEntity>>
    
    @Query("SELECT * FROM events WHERE notificationEnabled = 1 AND startDateTime > :currentTime ORDER BY startDateTime ASC")
    fun getUpcomingEventsWithNotifications(currentTime: LocalDateTime): Flow<List<EventEntity>>
    
    @Query("SELECT * FROM events WHERE title LIKE '%' || :query || '%' OR description LIKE '%' || :query || '%' ORDER BY startDateTime ASC")
    fun searchEvents(query: String): Flow<List<EventEntity>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvent(event: EventEntity): Long
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvents(events: List<EventEntity>)
    
    @Update
    suspend fun updateEvent(event: EventEntity)
    
    @Delete
    suspend fun deleteEvent(event: EventEntity)
    
    @Query("DELETE FROM events WHERE id = :id")
    suspend fun deleteEventById(id: Long)
    
    @Query("DELETE FROM events")
    suspend fun deleteAllEvents()
    
    @Query("SELECT COUNT(*) FROM events")
    suspend fun getEventCount(): Int
    
    @Query("SELECT COUNT(*) FROM events WHERE isCompleted = 1")
    suspend fun getCompletedEventCount(): Int
    
    @Query("SELECT COUNT(*) FROM events WHERE DATE(startDateTime) = DATE(:date)")
    suspend fun getEventCountForDate(date: LocalDateTime): Int
}