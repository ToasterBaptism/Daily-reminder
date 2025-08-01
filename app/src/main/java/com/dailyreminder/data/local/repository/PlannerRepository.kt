package com.dailyreminder.data.local.repository

import com.dailyreminder.data.local.database.dao.*
import com.dailyreminder.data.local.database.entity.*
import com.dailyreminder.data.model.*
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
    fun getAllEvents(): Flow<List<Event>> = 
        eventDao.getAllEvents().map { entities -> entities.map { it.toEvent() } }
    
    suspend fun getEventById(id: Long): Event? = 
        eventDao.getEventById(id)?.toEvent()
    
    fun getEventsByDate(date: LocalDateTime): Flow<List<Event>> = 
        eventDao.getEventsByDate(date).map { entities -> entities.map { it.toEvent() } }
    
    fun getEventsBetweenDates(startDate: LocalDateTime, endDate: LocalDateTime): Flow<List<Event>> = 
        eventDao.getEventsBetweenDates(startDate, endDate).map { entities -> entities.map { it.toEvent() } }
    
    fun getEventsByCategory(category: EventCategory): Flow<List<Event>> = 
        eventDao.getEventsByCategory(category).map { entities -> entities.map { it.toEvent() } }
    
    fun getUpcomingEventsWithNotifications(currentTime: LocalDateTime): Flow<List<Event>> = 
        eventDao.getUpcomingEventsWithNotifications(currentTime).map { entities -> entities.map { it.toEvent() } }
    
    fun searchEvents(query: String): Flow<List<Event>> = 
        eventDao.searchEvents(query).map { entities -> entities.map { it.toEvent() } }
    
    suspend fun insertEvent(event: Event): Long = 
        eventDao.insertEvent(event.toEntity())
    
    suspend fun updateEvent(event: Event) = 
        eventDao.updateEvent(event.toEntity())
    
    suspend fun deleteEvent(event: Event) = 
        eventDao.deleteEvent(event.toEntity())
    
    suspend fun deleteEventById(id: Long) = 
        eventDao.deleteEventById(id)
    
    // Meal operations
    fun getAllMeals(): Flow<List<Meal>> = 
        mealDao.getAllMeals().map { entities -> entities.map { it.toMeal() } }
    
    suspend fun getMealById(id: Long): Meal? = 
        mealDao.getMealById(id)?.toMeal()
    
    fun getMealsByDate(date: LocalDateTime): Flow<List<Meal>> = 
        mealDao.getMealsByDate(date).map { entities -> entities.map { it.toMeal() } }
    
    fun getMealsByType(mealType: MealType): Flow<List<Meal>> = 
        mealDao.getMealsByType(mealType).map { entities -> entities.map { it.toMeal() } }
    
    fun getMealByDateAndType(date: LocalDateTime, mealType: MealType): Flow<Meal?> = 
        mealDao.getMealByDateAndType(date, mealType).map { it?.toMeal() }
    
    fun getUpcomingMealsWithNotifications(currentTime: LocalDateTime): Flow<List<Meal>> = 
        mealDao.getUpcomingMealsWithNotifications(currentTime).map { entities -> entities.map { it.toMeal() } }
    
    fun searchMeals(query: String): Flow<List<Meal>> = 
        mealDao.searchMeals(query).map { entities -> entities.map { it.toMeal() } }
    
    suspend fun insertMeal(meal: Meal): Long = 
        mealDao.insertMeal(meal.toEntity())
    
    suspend fun updateMeal(meal: Meal) = 
        mealDao.updateMeal(meal.toEntity())
    
    suspend fun deleteMeal(meal: Meal) = 
        mealDao.deleteMeal(meal.toEntity())
    
    suspend fun deleteMealById(id: Long) = 
        mealDao.deleteMealById(id)
    
    // Task operations
    fun getAllTasks(): Flow<List<Task>> = 
        taskDao.getAllTasks().map { entities -> entities.map { it.toTask() } }
    
    suspend fun getTaskById(id: Long): Task? = 
        taskDao.getTaskById(id)?.toTask()
    
    fun getTasksByDate(date: LocalDateTime): Flow<List<Task>> = 
        taskDao.getTasksByDate(date).map { entities -> entities.map { it.toTask() } }
    
    fun getTasksByCategory(category: TaskCategory): Flow<List<Task>> = 
        taskDao.getTasksByCategory(category).map { entities -> entities.map { it.toTask() } }
    
    fun getTasksByCompletionStatus(isCompleted: Boolean): Flow<List<Task>> = 
        taskDao.getTasksByCompletionStatus(isCompleted).map { entities -> entities.map { it.toTask() } }
    
    fun getOverdueTasks(currentTime: LocalDateTime): Flow<List<Task>> = 
        taskDao.getOverdueTasks(currentTime).map { entities -> entities.map { it.toTask() } }
    
    fun getUpcomingTasksWithNotifications(currentTime: LocalDateTime): Flow<List<Task>> = 
        taskDao.getUpcomingTasksWithNotifications(currentTime).map { entities -> entities.map { it.toTask() } }
    
    fun searchTasks(query: String): Flow<List<Task>> = 
        taskDao.searchTasks(query).map { entities -> entities.map { it.toTask() } }
    
    suspend fun insertTask(task: Task): Long = 
        taskDao.insertTask(task.toEntity())
    
    suspend fun updateTask(task: Task) = 
        taskDao.updateTask(task.toEntity())
    
    suspend fun deleteTask(task: Task) = 
        taskDao.deleteTask(task.toEntity())
    
    suspend fun deleteTaskById(id: Long) = 
        taskDao.deleteTaskById(id)
    
    // Notification operations
    fun getAllNotifications(): Flow<List<Notification>> = 
        notificationDao.getAllNotifications().map { entities -> entities.map { it.toNotification() } }
    
    suspend fun getNotificationById(id: Long): Notification? = 
        notificationDao.getNotificationById(id)?.toNotification()
    
    suspend fun getPendingNotifications(currentTime: LocalDateTime): List<Notification> = 
        notificationDao.getPendingNotifications(currentTime).map { it.toNotification() }
    
    fun getActiveNotifications(): Flow<List<Notification>> = 
        notificationDao.getActiveNotifications().map { entities -> entities.map { it.toNotification() } }
    
    suspend fun getSnoozedNotificationsDue(currentTime: LocalDateTime): List<Notification> = 
        notificationDao.getSnoozedNotificationsDue(currentTime).map { it.toNotification() }
    
    fun getNotificationsByType(type: NotificationType): Flow<List<Notification>> = 
        notificationDao.getNotificationsByType(type).map { entities -> entities.map { it.toNotification() } }
    
    suspend fun insertNotification(notification: Notification): Long = 
        notificationDao.insertNotification(notification.toEntity())
    
    suspend fun updateNotification(notification: Notification) = 
        notificationDao.updateNotification(notification.toEntity())
    
    suspend fun deleteNotification(notification: Notification) = 
        notificationDao.deleteNotification(notification.toEntity())
    
    suspend fun markNotificationAsDelivered(id: Long, deliveredAt: LocalDateTime) = 
        notificationDao.markAsDelivered(id, deliveredAt)
    
    suspend fun markNotificationAsDismissed(id: Long, dismissedAt: LocalDateTime) = 
        notificationDao.markAsDismissed(id, dismissedAt)
    
    suspend fun snoozeNotification(id: Long, snoozeUntil: LocalDateTime) = 
        notificationDao.snoozeNotification(id, snoozeUntil)
    
    // Statistics
    suspend fun getEventCount(): Int = eventDao.getEventCount()
    suspend fun getMealCount(): Int = mealDao.getMealCount()
    suspend fun getTaskCount(): Int = taskDao.getTaskCount()
    suspend fun getCompletedTaskCount(): Int = taskDao.getCompletedTaskCount()
    suspend fun getAverageTaskProgress(): Double = taskDao.getAverageProgress()
}

// Extension functions to convert between entities and models
private fun EventEntity.toEvent(): Event = Event(
    id = id,
    title = title,
    description = description,
    startDateTime = startDateTime,
    endDateTime = endDateTime,
    category = category,
    priority = priority,
    recurrence = recurrence,
    isCompleted = isCompleted,
    notificationEnabled = notificationEnabled,
    notificationMinutesBefore = notificationMinutesBefore,
    createdAt = createdAt,
    updatedAt = updatedAt
)

private fun Event.toEntity(): EventEntity = EventEntity(
    id = id,
    title = title,
    description = description,
    startDateTime = startDateTime,
    endDateTime = endDateTime,
    category = category,
    priority = priority,
    recurrence = recurrence,
    isCompleted = isCompleted,
    notificationEnabled = notificationEnabled,
    notificationMinutesBefore = notificationMinutesBefore,
    createdAt = createdAt,
    updatedAt = updatedAt
)

private fun MealEntity.toMeal(): Meal = Meal(
    id = id,
    name = name,
    mealType = mealType,
    scheduledDateTime = scheduledDateTime,
    recipe = recipe,
    ingredients = ingredients,
    preparationTimeMinutes = preparationTimeMinutes,
    cookingTimeMinutes = cookingTimeMinutes,
    servings = servings,
    nutritionalInfo = nutritionalInfo,
    dietaryRestrictions = dietaryRestrictions,
    notes = notes,
    isCompleted = isCompleted,
    notificationEnabled = notificationEnabled,
    notificationMinutesBefore = notificationMinutesBefore,
    createdAt = createdAt,
    updatedAt = updatedAt
)

private fun Meal.toEntity(): MealEntity = MealEntity(
    id = id,
    name = name,
    mealType = mealType,
    scheduledDateTime = scheduledDateTime,
    recipe = recipe,
    ingredients = ingredients,
    preparationTimeMinutes = preparationTimeMinutes,
    cookingTimeMinutes = cookingTimeMinutes,
    servings = servings,
    nutritionalInfo = nutritionalInfo,
    dietaryRestrictions = dietaryRestrictions,
    notes = notes,
    isCompleted = isCompleted,
    notificationEnabled = notificationEnabled,
    notificationMinutesBefore = notificationMinutesBefore,
    createdAt = createdAt,
    updatedAt = updatedAt
)

private fun TaskEntity.toTask(): Task = Task(
    id = id,
    title = title,
    description = description,
    category = category,
    priority = priority,
    dueDateTime = dueDateTime,
    estimatedDurationMinutes = estimatedDurationMinutes,
    isCompleted = isCompleted,
    completedAt = completedAt,
    recurrence = recurrence,
    subtasks = subtasks,
    tags = tags,
    notificationEnabled = notificationEnabled,
    notificationMinutesBefore = notificationMinutesBefore,
    progress = progress,
    createdAt = createdAt,
    updatedAt = updatedAt
)

private fun Task.toEntity(): TaskEntity = TaskEntity(
    id = id,
    title = title,
    description = description,
    category = category,
    priority = priority,
    dueDateTime = dueDateTime,
    estimatedDurationMinutes = estimatedDurationMinutes,
    isCompleted = isCompleted,
    completedAt = completedAt,
    recurrence = recurrence,
    subtasks = subtasks,
    tags = tags,
    notificationEnabled = notificationEnabled,
    notificationMinutesBefore = notificationMinutesBefore,
    progress = progress,
    createdAt = createdAt,
    updatedAt = updatedAt
)

private fun NotificationEntity.toNotification(): Notification = Notification(
    id = id,
    title = title,
    message = message,
    type = type,
    relatedItemId = relatedItemId,
    scheduledDateTime = scheduledDateTime,
    isDelivered = isDelivered,
    deliveredAt = deliveredAt,
    isDismissed = isDismissed,
    dismissedAt = dismissedAt,
    isSnoozed = isSnoozed,
    snoozeUntil = snoozeUntil,
    soundEnabled = soundEnabled,
    vibrationEnabled = vibrationEnabled,
    ledEnabled = ledEnabled,
    priority = priority,
    actions = actions,
    createdAt = createdAt
)

private fun Notification.toEntity(): NotificationEntity = NotificationEntity(
    id = id,
    title = title,
    message = message,
    type = type,
    relatedItemId = relatedItemId,
    scheduledDateTime = scheduledDateTime,
    isDelivered = isDelivered,
    deliveredAt = deliveredAt,
    isDismissed = isDismissed,
    dismissedAt = dismissedAt,
    isSnoozed = isSnoozed,
    snoozeUntil = snoozeUntil,
    soundEnabled = soundEnabled,
    vibrationEnabled = vibrationEnabled,
    ledEnabled = ledEnabled,
    priority = priority,
    actions = actions,
    createdAt = createdAt
)