package com.dailyreminder.domain.usecase.notification

import com.dailyreminder.data.local.repository.PlannerRepository
import com.dailyreminder.data.model.*
import com.dailyreminder.data.util.DateUtils
import com.dailyreminder.domain.util.Result
import javax.inject.Inject

class ScheduleNotificationUseCase @Inject constructor(
    private val repository: PlannerRepository
) {
    
    suspend fun scheduleEventNotification(event: Event): Result<Long> {
        return try {
            if (!event.notificationEnabled) {
                return Result.Success(-1L) // No notification needed
            }
            
            val notificationTime = DateUtils.getTimeUntilNotification(
                event.startDateTime,
                event.notificationMinutesBefore
            )
            
            val notification = Notification(
                id = 0,
                title = "Upcoming Event",
                message = event.title,
                type = NotificationType.EVENT,
                relatedItemId = event.id,
                scheduledDateTime = notificationTime,
                isDelivered = false,
                deliveredAt = null,
                isDismissed = false,
                dismissedAt = null,
                isSnoozed = false,
                snoozeUntil = null,
                soundEnabled = true,
                vibrationEnabled = true,
                ledEnabled = true,
                priority = when (event.priority) {
                    Priority.LOW -> NotificationPriority.LOW
                    Priority.MEDIUM -> NotificationPriority.NORMAL
                    Priority.HIGH -> NotificationPriority.HIGH
                    Priority.URGENT -> NotificationPriority.HIGH
                },
                actions = listOf(
                    NotificationAction(
                        id = "view",
                        title = "View Event",
                        type = NotificationActionType.VIEW
                    ),
                    NotificationAction(
                        id = "dismiss",
                        title = "Dismiss",
                        type = NotificationActionType.DISMISS
                    )
                ),
                createdAt = DateUtils.now()
            )
            
            val notificationId = repository.insertNotification(notification)
            Result.Success(notificationId)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
    
    suspend fun scheduleMealNotification(meal: Meal): Result<Long> {
        return try {
            if (!meal.notificationEnabled) {
                return Result.Success(-1L)
            }
            
            val notificationTime = DateUtils.getTimeUntilNotification(
                meal.scheduledDateTime,
                meal.notificationMinutesBefore
            )
            
            val notification = Notification(
                id = 0,
                title = "Meal Time",
                message = "${meal.mealType.name.lowercase().replaceFirstChar { it.uppercase() }}: ${meal.name}",
                type = NotificationType.MEAL,
                relatedItemId = meal.id,
                scheduledDateTime = notificationTime,
                isDelivered = false,
                deliveredAt = null,
                isDismissed = false,
                dismissedAt = null,
                isSnoozed = false,
                snoozeUntil = null,
                soundEnabled = true,
                vibrationEnabled = true,
                ledEnabled = true,
                priority = NotificationPriority.NORMAL,
                actions = listOf(
                    NotificationAction(
                        id = "start_cooking",
                        title = "Start Cooking",
                        type = NotificationActionType.ACTION
                    ),
                    NotificationAction(
                        id = "snooze",
                        title = "Snooze 10min",
                        type = NotificationActionType.SNOOZE
                    )
                ),
                createdAt = DateUtils.now()
            )
            
            val notificationId = repository.insertNotification(notification)
            Result.Success(notificationId)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
    
    suspend fun scheduleTaskNotification(task: Task): Result<Long> {
        return try {
            if (!task.notificationEnabled || task.dueDateTime == null) {
                return Result.Success(-1L)
            }
            
            val notificationTime = DateUtils.getTimeUntilNotification(
                task.dueDateTime,
                task.notificationMinutesBefore
            )
            
            val notification = Notification(
                id = 0,
                title = "Task Due",
                message = task.title,
                type = NotificationType.TASK,
                relatedItemId = task.id,
                scheduledDateTime = notificationTime,
                isDelivered = false,
                deliveredAt = null,
                isDismissed = false,
                dismissedAt = null,
                isSnoozed = false,
                snoozeUntil = null,
                soundEnabled = true,
                vibrationEnabled = true,
                ledEnabled = true,
                priority = when (task.priority) {
                    Priority.LOW -> NotificationPriority.LOW
                    Priority.MEDIUM -> NotificationPriority.NORMAL
                    Priority.HIGH -> NotificationPriority.HIGH
                    Priority.URGENT -> NotificationPriority.HIGH
                },
                actions = listOf(
                    NotificationAction(
                        id = "complete",
                        title = "Mark Complete",
                        type = NotificationActionType.ACTION
                    ),
                    NotificationAction(
                        id = "snooze",
                        title = "Snooze 1hr",
                        type = NotificationActionType.SNOOZE
                    )
                ),
                createdAt = DateUtils.now()
            )
            
            val notificationId = repository.insertNotification(notification)
            Result.Success(notificationId)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}