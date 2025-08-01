package com.dailyreminder.data.local.repository

import com.dailyreminder.data.local.database.dao.EventDao
import com.dailyreminder.data.local.database.dao.MealDao
import com.dailyreminder.data.local.database.dao.NotificationDao
import com.dailyreminder.data.local.database.dao.TaskDao
import com.dailyreminder.data.local.database.entity.toEntity
import com.dailyreminder.data.local.database.entity.toEvent
import com.dailyreminder.data.local.database.entity.toMeal
import com.dailyreminder.data.local.database.entity.toNotification
import com.dailyreminder.data.local.database.entity.toTask
import com.dailyreminder.data.model.Event
import com.dailyreminder.data.model.Meal
import com.dailyreminder.data.model.Notification
import com.dailyreminder.data.model.Task
import com.dailyreminder.domain.util.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.LocalDateTime
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PlannerRepository @Inject constructor(
    private val eventDao: EventDao,
    private val mealDao: MealDao,
    private val taskDao: TaskDao,
    private val notificationDao: NotificationDao
) {

    // Event operations
    fun getAllEvents(): Flow<List<Event>> {
        return eventDao.getAllEvents().map { entities ->
            entities.map { it.toEvent() }
        }
    }

    suspend fun getEventById(id: Long): Result<Event> {
        return try {
            val entity = eventDao.getEventById(id)
            if (entity != null) {
                Result.Success(entity.toEvent())
            } else {
                Result.Error("Event not found")
            }
        } catch (e: Exception) {
            Result.Error(e.message ?: "Unknown error")
        }
    }

    fun getEventsBetweenDates(startDate: LocalDateTime, endDate: LocalDateTime): Flow<List<Event>> {
        return eventDao.getEventsBetweenDates(startDate.toString(), endDate.toString())
            .map { entities -> entities.map { it.toEvent() } }
    }

    suspend fun insertEvent(event: Event): Result<Long> {
        return try {
            val id = eventDao.insertEvent(event.toEntity())
            Result.Success(id)
        } catch (e: Exception) {
            Result.Error(e.message ?: "Failed to insert event")
        }
    }

    suspend fun updateEvent(event: Event): Result<Unit> {
        return try {
            eventDao.updateEvent(event.toEntity())
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e.message ?: "Failed to update event")
        }
    }

    suspend fun deleteEvent(event: Event): Result<Unit> {
        return try {
            eventDao.deleteEvent(event.toEntity())
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e.message ?: "Failed to delete event")
        }
    }

    // Meal operations
    fun getAllMeals(): Flow<List<Meal>> {
        return mealDao.getAllMeals().map { entities ->
            entities.map { it.toMeal() }
        }
    }

    suspend fun getMealById(id: Long): Result<Meal> {
        return try {
            val entity = mealDao.getMealById(id)
            if (entity != null) {
                Result.Success(entity.toMeal())
            } else {
                Result.Error("Meal not found")
            }
        } catch (e: Exception) {
            Result.Error(e.message ?: "Unknown error")
        }
    }

    fun getMealsBetweenDates(startDate: LocalDateTime, endDate: LocalDateTime): Flow<List<Meal>> {
        return mealDao.getMealsBetweenDates(startDate.toString(), endDate.toString())
            .map { entities -> entities.map { it.toMeal() } }
    }

    suspend fun insertMeal(meal: Meal): Result<Long> {
        return try {
            val id = mealDao.insertMeal(meal.toEntity())
            Result.Success(id)
        } catch (e: Exception) {
            Result.Error(e.message ?: "Failed to insert meal")
        }
    }

    suspend fun updateMeal(meal: Meal): Result<Unit> {
        return try {
            mealDao.updateMeal(meal.toEntity())
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e.message ?: "Failed to update meal")
        }
    }

    suspend fun deleteMeal(meal: Meal): Result<Unit> {
        return try {
            mealDao.deleteMeal(meal.toEntity())
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e.message ?: "Failed to delete meal")
        }
    }

    // Task operations
    fun getAllTasks(): Flow<List<Task>> {
        return taskDao.getAllTasks().map { entities ->
            entities.map { it.toTask() }
        }
    }

    suspend fun getTaskById(id: Long): Result<Task> {
        return try {
            val entity = taskDao.getTaskById(id)
            if (entity != null) {
                Result.Success(entity.toTask())
            } else {
                Result.Error("Task not found")
            }
        } catch (e: Exception) {
            Result.Error(e.message ?: "Unknown error")
        }
    }

    fun getTasksByCompletionStatus(isCompleted: Boolean): Flow<List<Task>> {
        return taskDao.getTasksByCompletionStatus(isCompleted)
            .map { entities -> entities.map { it.toTask() } }
    }

    suspend fun insertTask(task: Task): Result<Long> {
        return try {
            val id = taskDao.insertTask(task.toEntity())
            Result.Success(id)
        } catch (e: Exception) {
            Result.Error(e.message ?: "Failed to insert task")
        }
    }

    suspend fun updateTask(task: Task): Result<Unit> {
        return try {
            taskDao.updateTask(task.toEntity())
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e.message ?: "Failed to update task")
        }
    }

    suspend fun deleteTask(task: Task): Result<Unit> {
        return try {
            taskDao.deleteTask(task.toEntity())
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e.message ?: "Failed to delete task")
        }
    }

    // Notification operations
    fun getAllNotifications(): Flow<List<Notification>> {
        return notificationDao.getAllNotifications().map { entities ->
            entities.map { it.toNotification() }
        }
    }

    suspend fun insertNotification(notification: Notification): Result<Long> {
        return try {
            val id = notificationDao.insertNotification(notification.toEntity())
            Result.Success(id)
        } catch (e: Exception) {
            Result.Error(e.message ?: "Failed to insert notification")
        }
    }

    suspend fun updateNotification(notification: Notification): Result<Unit> {
        return try {
            notificationDao.updateNotification(notification.toEntity())
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e.message ?: "Failed to update notification")
        }
    }

    suspend fun getPendingNotifications(currentDateTime: LocalDateTime): Result<List<Notification>> {
        return try {
            val entities = notificationDao.getPendingNotifications(currentDateTime.toString())
            Result.Success(entities.map { it.toNotification() })
        } catch (e: Exception) {
            Result.Error(e.message ?: "Failed to get pending notifications")
        }
    }
}