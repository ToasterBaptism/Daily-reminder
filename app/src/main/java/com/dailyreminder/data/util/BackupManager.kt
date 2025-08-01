package com.dailyreminder.data.util

import android.content.Context
import com.dailyreminder.data.local.repository.PlannerRepository
import com.dailyreminder.data.model.*
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import kotlinx.coroutines.flow.first
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.serializers.LocalDateTimeIso8601Serializer
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.zip.GZIPInputStream
import java.util.zip.GZIPOutputStream
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BackupManager @Inject constructor(
    private val context: Context,
    private val repository: PlannerRepository,
    private val encryptionUtils: EncryptionUtils
) {
    
    companion object {
        private const val BACKUP_DIRECTORY = "backups"
        private const val BACKUP_FILE_EXTENSION = ".drb" // Daily Reminder Backup
        private const val BACKUP_VERSION = 1
    }
    
    private val gson: Gson = GsonBuilder()
        .registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeIso8601Serializer)
        .setPrettyPrinting()
        .create()
    
    private val backupDirectory: File by lazy {
        File(context.filesDir, BACKUP_DIRECTORY).apply {
            if (!exists()) mkdirs()
        }
    }
    
    suspend fun createBackup(
        fileName: String? = null,
        password: String? = null,
        includeCompletedItems: Boolean = true
    ): BackupResult {
        return try {
            val backupData = collectBackupData(includeCompletedItems)
            val backupJson = gson.toJson(backupData)
            
            val finalFileName = fileName ?: generateBackupFileName()
            val backupFile = File(backupDirectory, "$finalFileName$BACKUP_FILE_EXTENSION")
            
            // Compress and optionally encrypt
            val compressedData = compressData(backupJson.toByteArray())
            val finalData = if (password != null) {
                encryptionUtils.encryptData(compressedData, password).let { encrypted ->
                    encrypted.iv + encrypted.data
                }
            } else {
                compressedData
            }
            
            FileOutputStream(backupFile).use { it.write(finalData) }
            
            BackupResult.Success(
                fileName = backupFile.name,
                filePath = backupFile.absolutePath,
                size = backupFile.length(),
                itemCount = backupData.getTotalItemCount(),
                isEncrypted = password != null
            )
        } catch (e: Exception) {
            BackupResult.Error("Failed to create backup: ${e.message}")
        }
    }
    
    suspend fun restoreBackup(
        filePath: String,
        password: String? = null,
        replaceExisting: Boolean = false
    ): RestoreResult {
        return try {
            val backupFile = File(filePath)
            if (!backupFile.exists()) {
                return RestoreResult.Error("Backup file not found")
            }
            
            val fileData = FileInputStream(backupFile).use { it.readBytes() }
            
            // Decrypt if password provided
            val decompressedData = if (password != null) {
                val iv = fileData.sliceArray(0..11) // 12 bytes IV
                val encryptedData = fileData.sliceArray(12 until fileData.size)
                val decryptedData = encryptionUtils.decryptData(
                    EncryptionUtils.EncryptedData(encryptedData, iv),
                    password
                )
                decompressData(decryptedData)
            } else {
                decompressData(fileData)
            }
            
            val backupJson = String(decompressedData, Charsets.UTF_8)
            val backupData = gson.fromJson(backupJson, BackupData::class.java)
            
            // Validate backup version
            if (backupData.version > BACKUP_VERSION) {
                return RestoreResult.Error("Backup version not supported")
            }
            
            // Clear existing data if requested
            if (replaceExisting) {
                clearAllData()
            }
            
            // Restore data
            restoreBackupData(backupData)
            
            RestoreResult.Success(
                itemCount = backupData.getTotalItemCount(),
                backupDate = backupData.createdAt
            )
        } catch (e: Exception) {
            RestoreResult.Error("Failed to restore backup: ${e.message}")
        }
    }
    
    fun getBackupFiles(): List<BackupFileInfo> {
        return backupDirectory.listFiles { _, name -> 
            name.endsWith(BACKUP_FILE_EXTENSION) 
        }?.map { file ->
            BackupFileInfo(
                name = file.name,
                path = file.absolutePath,
                size = file.length(),
                lastModified = LocalDateTime.parse(
                    java.time.Instant.ofEpochMilli(file.lastModified())
                        .atZone(java.time.ZoneId.systemDefault())
                        .toLocalDateTime()
                        .toString()
                )
            )
        }?.sortedByDescending { it.lastModified } ?: emptyList()
    }
    
    fun deleteBackupFile(filePath: String): Boolean {
        return try {
            File(filePath).delete()
        } catch (e: Exception) {
            false
        }
    }
    
    private suspend fun collectBackupData(includeCompletedItems: Boolean): BackupData {
        val events = repository.getAllEvents().first().let { events ->
            if (includeCompletedItems) events else events.filter { !it.isCompleted }
        }
        
        val meals = repository.getAllMeals().first().let { meals ->
            if (includeCompletedItems) meals else meals.filter { !it.isCompleted }
        }
        
        val tasks = repository.getAllTasks().first().let { tasks ->
            if (includeCompletedItems) tasks else tasks.filter { !it.isCompleted }
        }
        
        val notifications = repository.getAllNotifications().first()
        
        return BackupData(
            version = BACKUP_VERSION,
            createdAt = DateUtils.now(),
            events = events,
            meals = meals,
            tasks = tasks,
            notifications = notifications
        )
    }
    
    private suspend fun restoreBackupData(backupData: BackupData) {
        // Insert events
        backupData.events.forEach { event ->
            repository.insertEvent(event.copy(id = 0)) // Reset ID for new insertion
        }
        
        // Insert meals
        backupData.meals.forEach { meal ->
            repository.insertMeal(meal.copy(id = 0))
        }
        
        // Insert tasks
        backupData.tasks.forEach { task ->
            repository.insertTask(task.copy(id = 0))
        }
        
        // Insert notifications
        backupData.notifications.forEach { notification ->
            repository.insertNotification(notification.copy(id = 0))
        }
    }
    
    private suspend fun clearAllData() {
        // This would require additional methods in the repository
        // For now, we'll skip this implementation
    }
    
    private fun compressData(data: ByteArray): ByteArray {
        val outputStream = java.io.ByteArrayOutputStream()
        GZIPOutputStream(outputStream).use { gzip ->
            gzip.write(data)
        }
        return outputStream.toByteArray()
    }
    
    private fun decompressData(compressedData: ByteArray): ByteArray {
        val inputStream = java.io.ByteArrayInputStream(compressedData)
        return GZIPInputStream(inputStream).use { gzip ->
            gzip.readBytes()
        }
    }
    
    private fun generateBackupFileName(): String {
        val now = DateUtils.now()
        return "backup_${now.date}_${now.hour}${now.minute}"
    }
    
    data class BackupData(
        val version: Int,
        val createdAt: LocalDateTime,
        val events: List<Event>,
        val meals: List<Meal>,
        val tasks: List<Task>,
        val notifications: List<Notification>
    ) {
        fun getTotalItemCount(): Int = events.size + meals.size + tasks.size + notifications.size
    }
    
    data class BackupFileInfo(
        val name: String,
        val path: String,
        val size: Long,
        val lastModified: LocalDateTime
    )
    
    sealed class BackupResult {
        data class Success(
            val fileName: String,
            val filePath: String,
            val size: Long,
            val itemCount: Int,
            val isEncrypted: Boolean
        ) : BackupResult()
        
        data class Error(val message: String) : BackupResult()
    }
    
    sealed class RestoreResult {
        data class Success(
            val itemCount: Int,
            val backupDate: LocalDateTime
        ) : RestoreResult()
        
        data class Error(val message: String) : RestoreResult()
    }
}