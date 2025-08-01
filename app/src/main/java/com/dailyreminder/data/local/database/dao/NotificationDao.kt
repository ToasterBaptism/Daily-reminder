package com.dailyreminder.data.local.database.dao

import androidx.room.*
import com.dailyreminder.data.local.database.entity.NotificationEntity
import com.dailyreminder.data.model.NotificationPriority
import com.dailyreminder.data.model.NotificationType
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDateTime

@Dao
interface NotificationDao {
    
    @Query("SELECT * FROM notifications ORDER BY scheduledDateTime DESC")
    fun getAllNotifications(): Flow<List<NotificationEntity>>
    
    @Query("SELECT * FROM notifications WHERE id = :id")
    suspend fun getNotificationById(id: Long): NotificationEntity?
    
    @Query("SELECT * FROM notifications WHERE scheduledDateTime <= :currentTime AND isDelivered = 0 ORDER BY scheduledDateTime ASC")
    suspend fun getPendingNotifications(currentTime: LocalDateTime): List<NotificationEntity>
    
    @Query("SELECT * FROM notifications WHERE isDelivered = 1 ORDER BY deliveredAt DESC")
    fun getDeliveredNotifications(): Flow<List<NotificationEntity>>
    
    @Query("SELECT * FROM notifications WHERE isDismissed = 0 AND isDelivered = 1 ORDER BY deliveredAt DESC")
    fun getActiveNotifications(): Flow<List<NotificationEntity>>
    
    @Query("SELECT * FROM notifications WHERE isSnoozed = 1 AND snoozeUntil <= :currentTime")
    suspend fun getSnoozedNotificationsDue(currentTime: LocalDateTime): List<NotificationEntity>
    
    @Query("SELECT * FROM notifications WHERE type = :type ORDER BY scheduledDateTime DESC")
    fun getNotificationsByType(type: NotificationType): Flow<List<NotificationEntity>>
    
    @Query("SELECT * FROM notifications WHERE priority = :priority ORDER BY scheduledDateTime DESC")
    fun getNotificationsByPriority(priority: NotificationPriority): Flow<List<NotificationEntity>>
    
    @Query("SELECT * FROM notifications WHERE relatedItemId = :itemId AND type = :type")
    fun getNotificationsForItem(itemId: Long, type: NotificationType): Flow<List<NotificationEntity>>
    
    @Query("SELECT * FROM notifications WHERE scheduledDateTime BETWEEN :startDate AND :endDate ORDER BY scheduledDateTime ASC")
    fun getNotificationsBetweenDates(startDate: LocalDateTime, endDate: LocalDateTime): Flow<List<NotificationEntity>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotification(notification: NotificationEntity): Long
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotifications(notifications: List<NotificationEntity>)
    
    @Update
    suspend fun updateNotification(notification: NotificationEntity)
    
    @Delete
    suspend fun deleteNotification(notification: NotificationEntity)
    
    @Query("DELETE FROM notifications WHERE id = :id")
    suspend fun deleteNotificationById(id: Long)
    
    @Query("DELETE FROM notifications WHERE relatedItemId = :itemId AND type = :type")
    suspend fun deleteNotificationsForItem(itemId: Long, type: NotificationType)
    
    @Query("DELETE FROM notifications WHERE isDelivered = 1 AND deliveredAt < :cutoffDate")
    suspend fun deleteOldDeliveredNotifications(cutoffDate: LocalDateTime)
    
    @Query("DELETE FROM notifications")
    suspend fun deleteAllNotifications()
    
    @Query("UPDATE notifications SET isDelivered = 1, deliveredAt = :deliveredAt WHERE id = :id")
    suspend fun markAsDelivered(id: Long, deliveredAt: LocalDateTime)
    
    @Query("UPDATE notifications SET isDismissed = 1, dismissedAt = :dismissedAt WHERE id = :id")
    suspend fun markAsDismissed(id: Long, dismissedAt: LocalDateTime)
    
    @Query("UPDATE notifications SET isSnoozed = 1, snoozeUntil = :snoozeUntil WHERE id = :id")
    suspend fun snoozeNotification(id: Long, snoozeUntil: LocalDateTime)
    
    @Query("UPDATE notifications SET isSnoozed = 0, snoozeUntil = NULL WHERE id = :id")
    suspend fun unsnoozeNotification(id: Long)
    
    @Query("SELECT COUNT(*) FROM notifications")
    suspend fun getNotificationCount(): Int
    
    @Query("SELECT COUNT(*) FROM notifications WHERE isDelivered = 0")
    suspend fun getPendingNotificationCount(): Int
    
    @Query("SELECT COUNT(*) FROM notifications WHERE isDismissed = 0 AND isDelivered = 1")
    suspend fun getActiveNotificationCount(): Int
}