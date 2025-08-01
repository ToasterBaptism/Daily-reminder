package com.dailyreminder.data.local.database.dao

import androidx.room.*
import com.dailyreminder.data.local.database.entity.TaskEntity
import com.dailyreminder.data.model.Priority
import com.dailyreminder.data.model.TaskCategory
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDateTime

@Dao
interface TaskDao {
    
    @Query("SELECT * FROM tasks ORDER BY dueDateTime ASC")
    fun getAllTasks(): Flow<List<TaskEntity>>
    
    @Query("SELECT * FROM tasks WHERE id = :id")
    suspend fun getTaskById(id: Long): TaskEntity?
    
    @Query("SELECT * FROM tasks WHERE DATE(dueDateTime) = DATE(:date) ORDER BY dueDateTime ASC")
    fun getTasksByDate(date: LocalDateTime): Flow<List<TaskEntity>>
    
    @Query("SELECT * FROM tasks WHERE dueDateTime BETWEEN :startDate AND :endDate ORDER BY dueDateTime ASC")
    fun getTasksBetweenDates(startDate: LocalDateTime, endDate: LocalDateTime): Flow<List<TaskEntity>>
    
    @Query("SELECT * FROM tasks WHERE category = :category ORDER BY dueDateTime ASC")
    fun getTasksByCategory(category: TaskCategory): Flow<List<TaskEntity>>
    
    @Query("SELECT * FROM tasks WHERE priority = :priority ORDER BY dueDateTime ASC")
    fun getTasksByPriority(priority: Priority): Flow<List<TaskEntity>>
    
    @Query("SELECT * FROM tasks WHERE isCompleted = :isCompleted ORDER BY dueDateTime ASC")
    fun getTasksByCompletionStatus(isCompleted: Boolean): Flow<List<TaskEntity>>
    
    @Query("SELECT * FROM tasks WHERE dueDateTime < :currentTime AND isCompleted = 0 ORDER BY dueDateTime ASC")
    fun getOverdueTasks(currentTime: LocalDateTime): Flow<List<TaskEntity>>
    
    @Query("SELECT * FROM tasks WHERE notificationEnabled = 1 AND dueDateTime > :currentTime AND isCompleted = 0 ORDER BY dueDateTime ASC")
    fun getUpcomingTasksWithNotifications(currentTime: LocalDateTime): Flow<List<TaskEntity>>
    
    @Query("SELECT * FROM tasks WHERE title LIKE '%' || :query || '%' OR description LIKE '%' || :query || '%' ORDER BY dueDateTime ASC")
    fun searchTasks(query: String): Flow<List<TaskEntity>>
    
    @Query("SELECT * FROM tasks WHERE tags LIKE '%' || :tag || '%' ORDER BY dueDateTime ASC")
    fun getTasksByTag(tag: String): Flow<List<TaskEntity>>
    
    @Query("SELECT * FROM tasks WHERE progress BETWEEN :minProgress AND :maxProgress ORDER BY dueDateTime ASC")
    fun getTasksByProgressRange(minProgress: Int, maxProgress: Int): Flow<List<TaskEntity>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: TaskEntity): Long
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTasks(tasks: List<TaskEntity>)
    
    @Update
    suspend fun updateTask(task: TaskEntity)
    
    @Delete
    suspend fun deleteTask(task: TaskEntity)
    
    @Query("DELETE FROM tasks WHERE id = :id")
    suspend fun deleteTaskById(id: Long)
    
    @Query("DELETE FROM tasks")
    suspend fun deleteAllTasks()
    
    @Query("SELECT COUNT(*) FROM tasks")
    suspend fun getTaskCount(): Int
    
    @Query("SELECT COUNT(*) FROM tasks WHERE isCompleted = 1")
    suspend fun getCompletedTaskCount(): Int
    
    @Query("SELECT COUNT(*) FROM tasks WHERE DATE(dueDateTime) = DATE(:date)")
    suspend fun getTaskCountForDate(date: LocalDateTime): Int
    
    @Query("SELECT AVG(progress) FROM tasks WHERE isCompleted = 0")
    suspend fun getAverageProgress(): Double
}