package com.dailyreminder.data.local.database.dao

import androidx.room.*
import com.dailyreminder.data.local.database.entity.NotificationEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NotificationDao {

    @Query("SELECT * FROM notifications ORDER BY scheduledDateTime DESC")
    fun getAllNotifications(): Flow<List<NotificationEntity>>

    @Query("SELECT * FROM notifications WHERE id = :id")
    suspend fun getNotificationById(id: Long): NotificationEntity?

    @Query("SELECT * FROM notifications WHERE type = :type ORDER BY scheduledDateTime DESC")
    fun getNotificationsByType(type: String): Flow<List<NotificationEntity>>

    @Query("SELECT * FROM notifications WHERE relatedItemId = :itemId AND type = :type")
    suspend fun getNotificationByRelatedItem(itemId: Long, type: String): NotificationEntity?

    @Query("SELECT * FROM notifications WHERE isDelivered = :isDelivered ORDER BY scheduledDateTime DESC")
    fun getNotificationsByDeliveryStatus(isDelivered: Boolean): Flow<List<NotificationEntity>>

    @Query("SELECT * FROM notifications WHERE isDismissed = :isDismissed ORDER BY scheduledDateTime DESC")
    fun getNotificationsByDismissalStatus(isDismissed: Boolean): Flow<List<NotificationEntity>>

    @Query("SELECT * FROM notifications WHERE scheduledDateTime <= :currentDateTime AND isDelivered = 0 ORDER BY scheduledDateTime ASC")
    suspend fun getPendingNotifications(currentDateTime: String): List<NotificationEntity>

    @Query("SELECT * FROM notifications WHERE scheduledDateTime BETWEEN :startDate AND :endDate ORDER BY scheduledDateTime ASC")
    fun getNotificationsBetweenDates(startDate: String, endDate: String): Flow<List<NotificationEntity>>

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
    suspend fun deleteNotificationsByRelatedItem(itemId: Long, type: String)

    @Query("DELETE FROM notifications")
    suspend fun deleteAllNotifications()

    @Query("DELETE FROM notifications WHERE scheduledDateTime < :cutoffDate AND isDelivered = 1")
    suspend fun deleteOldDeliveredNotifications(cutoffDate: String)

    @Query("SELECT COUNT(*) FROM notifications")
    suspend fun getNotificationCount(): Int

    @Query("SELECT COUNT(*) FROM notifications WHERE isDelivered = 0")
    suspend fun getPendingNotificationCount(): Int
}