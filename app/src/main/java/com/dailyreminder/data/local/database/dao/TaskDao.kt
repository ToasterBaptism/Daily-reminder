package com.dailyreminder.data.local.database.dao

import androidx.room.*
import com.dailyreminder.data.local.database.entity.TaskEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {

    @Query("SELECT * FROM tasks ORDER BY dueDateTime ASC")
    fun getAllTasks(): Flow<List<TaskEntity>>

    @Query("SELECT * FROM tasks WHERE id = :id")
    suspend fun getTaskById(id: Long): TaskEntity?

    @Query("SELECT * FROM tasks WHERE dueDateTime BETWEEN :startDate AND :endDate ORDER BY dueDateTime ASC")
    fun getTasksBetweenDates(startDate: String, endDate: String): Flow<List<TaskEntity>>

    @Query("SELECT * FROM tasks WHERE category = :category ORDER BY dueDateTime ASC")
    fun getTasksByCategory(category: String): Flow<List<TaskEntity>>

    @Query("SELECT * FROM tasks WHERE priority = :priority ORDER BY dueDateTime ASC")
    fun getTasksByPriority(priority: String): Flow<List<TaskEntity>>

    @Query("SELECT * FROM tasks WHERE isCompleted = :isCompleted ORDER BY dueDateTime ASC")
    fun getTasksByCompletionStatus(isCompleted: Boolean): Flow<List<TaskEntity>>

    @Query("SELECT * FROM tasks WHERE title LIKE '%' || :searchQuery || '%' OR description LIKE '%' || :searchQuery || '%' ORDER BY dueDateTime ASC")
    fun searchTasks(searchQuery: String): Flow<List<TaskEntity>>

    @Query("SELECT * FROM tasks WHERE recurrence != 'NONE' ORDER BY dueDateTime ASC")
    fun getRecurringTasks(): Flow<List<TaskEntity>>

    @Query("SELECT * FROM tasks WHERE tags LIKE '%' || :tag || '%' ORDER BY dueDateTime ASC")
    fun getTasksByTag(tag: String): Flow<List<TaskEntity>>

    @Query("SELECT * FROM tasks WHERE isCompleted = 0 AND dueDateTime IS NOT NULL ORDER BY dueDateTime ASC")
    fun getUpcomingTasks(): Flow<List<TaskEntity>>

    @Query("SELECT * FROM tasks WHERE isCompleted = 0 AND dueDateTime < :currentDateTime ORDER BY dueDateTime ASC")
    fun getOverdueTasks(currentDateTime: String): Flow<List<TaskEntity>>

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

    @Query("SELECT COUNT(*) FROM tasks WHERE isCompleted = 0")
    suspend fun getPendingTaskCount(): Int

    @Query("SELECT COUNT(*) FROM tasks WHERE isCompleted = 0 AND dueDateTime < :currentDateTime")
    suspend fun getOverdueTaskCount(currentDateTime: String): Int
}