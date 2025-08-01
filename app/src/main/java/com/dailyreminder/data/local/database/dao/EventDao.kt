package com.dailyreminder.data.local.database.dao

import androidx.room.*
import com.dailyreminder.data.local.database.entity.EventEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface EventDao {

    @Query("SELECT * FROM events ORDER BY startDateTime ASC")
    fun getAllEvents(): Flow<List<EventEntity>>

    @Query("SELECT * FROM events WHERE id = :id")
    suspend fun getEventById(id: Long): EventEntity?

    @Query("SELECT * FROM events WHERE startDateTime BETWEEN :startDate AND :endDate ORDER BY startDateTime ASC")
    fun getEventsBetweenDates(startDate: String, endDate: String): Flow<List<EventEntity>>

    @Query("SELECT * FROM events WHERE category = :category ORDER BY startDateTime ASC")
    fun getEventsByCategory(category: String): Flow<List<EventEntity>>

    @Query("SELECT * FROM events WHERE isCompleted = :isCompleted ORDER BY startDateTime ASC")
    fun getEventsByCompletionStatus(isCompleted: Boolean): Flow<List<EventEntity>>

    @Query("SELECT * FROM events WHERE title LIKE '%' || :searchQuery || '%' OR description LIKE '%' || :searchQuery || '%' ORDER BY startDateTime ASC")
    fun searchEvents(searchQuery: String): Flow<List<EventEntity>>

    @Query("SELECT * FROM events WHERE recurrence != 'NONE' ORDER BY startDateTime ASC")
    fun getRecurringEvents(): Flow<List<EventEntity>>

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

    @Query("SELECT COUNT(*) FROM events WHERE isCompleted = 0 AND startDateTime < :currentDateTime")
    suspend fun getOverdueEventCount(currentDateTime: String): Int
}