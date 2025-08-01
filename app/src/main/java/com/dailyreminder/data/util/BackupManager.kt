package com.dailyreminder.data.util

import android.content.Context
import com.dailyreminder.data.local.repository.PlannerRepository
import com.dailyreminder.data.model.Event
import com.dailyreminder.data.model.Meal
import com.dailyreminder.data.model.Task
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import kotlinx.coroutines.flow.first
import kotlinx.datetime.LocalDateTime
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream
import java.util.zip.ZipOutputStream
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BackupManager @Inject constructor(
    private val context: Context,
    private val repository: PlannerRepository,
    private val encryptionUtils: EncryptionUtils
) {

    private val gson: Gson = GsonBuilder()
        .setPrettyPrinting()
        .create()

    data class BackupData(
        val version: Int = 1,
        val timestamp: String,
        val deviceInfo: String,
        val events: List<Event>,
        val meals: List<Meal>,
        val tasks: List<Task>
    )

    suspend fun createBackup(password: String? = null): Result<File> {
        return try {
            val events = repository.getAllEvents().first()
            val meals = repository.getAllMeals().first()
            val tasks = repository.getAllTasks().first()

            val backupData = BackupData(
                timestamp = DateUtils.getCurrentDateTime().toString(),
                deviceInfo = "${android.os.Build.MANUFACTURER} ${android.os.Build.MODEL}",
                events = events,
                meals = meals,
                tasks = tasks
            )

            val jsonData = gson.toJson(backupData)
            val finalData = if (password != null) {
                encryptionUtils.encryptToBase64(jsonData) ?: return Result.Error("Encryption failed")
            } else {
                jsonData
            }

            val backupDir = File(context.getExternalFilesDir(null), "backups")
            if (!backupDir.exists()) {
                backupDir.mkdirs()
            }

            val timestamp = DateUtils.getCurrentDateTime().toString().replace(":", "-")
            val fileName = "daily_reminder_backup_$timestamp.json"
            val backupFile = File(backupDir, fileName)

            // Create compressed backup
            val zipFile = File(backupDir, fileName.replace(".json", ".zip"))
            ZipOutputStream(FileOutputStream(zipFile)).use { zipOut ->
                val entry = ZipEntry(fileName)
                zipOut.putNextEntry(entry)
                zipOut.write(finalData.toByteArray())
                zipOut.closeEntry()
            }

            Result.Success(zipFile)
        } catch (e: Exception) {
            Result.Error("Backup creation failed: ${e.message}")
        }
    }

    suspend fun restoreBackup(backupFile: File, password: String? = null): Result<Unit> {
        return try {
            val jsonData = if (backupFile.extension == "zip") {
                extractFromZip(backupFile)
            } else {
                backupFile.readText()
            }

            val decryptedData = if (password != null) {
                encryptionUtils.decryptFromBase64(jsonData) ?: return Result.Error("Decryption failed")
            } else {
                jsonData
            }

            val backupData = gson.fromJson(decryptedData, BackupData::class.java)

            // Restore data to database
            backupData.events.forEach { event ->
                repository.insertEvent(event.copy(id = 0)) // Reset ID for new insertion
            }

            backupData.meals.forEach { meal ->
                repository.insertMeal(meal.copy(id = 0))
            }

            backupData.tasks.forEach { task ->
                repository.insertTask(task.copy(id = 0))
            }

            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error("Backup restoration failed: ${e.message}")
        }
    }

    private fun extractFromZip(zipFile: File): String {
        ZipInputStream(FileInputStream(zipFile)).use { zipIn ->
            var entry = zipIn.nextEntry
            while (entry != null) {
                if (!entry.isDirectory && entry.name.endsWith(".json")) {
                    return zipIn.readBytes().toString(Charsets.UTF_8)
                }
                entry = zipIn.nextEntry
            }
        }
        throw IllegalArgumentException("No JSON file found in backup")
    }

    fun getBackupFiles(): List<File> {
        val backupDir = File(context.getExternalFilesDir(null), "backups")
        return if (backupDir.exists()) {
            backupDir.listFiles { file ->
                file.extension in listOf("json", "zip") && file.name.startsWith("daily_reminder_backup_")
            }?.sortedByDescending { it.lastModified() } ?: emptyList()
        } else {
            emptyList()
        }
    }

    fun deleteBackup(backupFile: File): Boolean {
        return backupFile.delete()
    }

    fun getBackupInfo(backupFile: File): BackupInfo? {
        return try {
            val jsonData = if (backupFile.extension == "zip") {
                extractFromZip(backupFile)
            } else {
                backupFile.readText()
            }

            // Try to parse without decryption first
            val backupData = try {
                gson.fromJson(jsonData, BackupData::class.java)
            } catch (e: Exception) {
                // If parsing fails, it might be encrypted
                return BackupInfo(
                    fileName = backupFile.name,
                    timestamp = "Encrypted",
                    deviceInfo = "Unknown",
                    eventCount = 0,
                    mealCount = 0,
                    taskCount = 0,
                    isEncrypted = true,
                    fileSize = backupFile.length()
                )
            }

            BackupInfo(
                fileName = backupFile.name,
                timestamp = backupData.timestamp,
                deviceInfo = backupData.deviceInfo,
                eventCount = backupData.events.size,
                mealCount = backupData.meals.size,
                taskCount = backupData.tasks.size,
                isEncrypted = false,
                fileSize = backupFile.length()
            )
        } catch (e: Exception) {
            null
        }
    }

    data class BackupInfo(
        val fileName: String,
        val timestamp: String,
        val deviceInfo: String,
        val eventCount: Int,
        val mealCount: Int,
        val taskCount: Int,
        val isEncrypted: Boolean,
        val fileSize: Long
    )

    sealed class Result<out T> {
        data class Success<out T>(val data: T) : Result<T>()
        data class Error(val message: String) : Result<Nothing>()
    }
}