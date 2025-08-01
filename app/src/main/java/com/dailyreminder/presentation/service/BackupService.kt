package com.dailyreminder.presentation.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.dailyreminder.R
import com.dailyreminder.data.util.BackupManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import javax.inject.Inject

@AndroidEntryPoint
class BackupService : Service() {
    
    @Inject
    lateinit var backupManager: BackupManager
    
    private val serviceScope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    
    companion object {
        const val ACTION_BACKUP = "com.dailyreminder.action.BACKUP"
        const val ACTION_RESTORE = "com.dailyreminder.action.RESTORE"
        const val EXTRA_BACKUP_PATH = "backup_path"
        const val NOTIFICATION_ID_BACKUP = 5000
    }
    
    override fun onBind(intent: Intent?): IBinder? = null
    
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_BACKUP -> performBackup()
            ACTION_RESTORE -> {
                val backupPath = intent.getStringExtra(EXTRA_BACKUP_PATH)
                if (backupPath != null) {
                    performRestore(backupPath)
                }
            }
        }
        return START_NOT_STICKY
    }
    
    private fun performBackup() {
        serviceScope.launch {
            try {
                showBackupNotification("Creating backup...", true)
                
                val backupFile = backupManager.createBackup()
                
                showBackupNotification("Backup created successfully: ${backupFile.name}", false)
                
            } catch (e: Exception) {
                showBackupNotification("Backup failed: ${e.message}", false)
            } finally {
                stopSelf()
            }
        }
    }
    
    private fun performRestore(backupPath: String) {
        serviceScope.launch {
            try {
                showBackupNotification("Restoring from backup...", true)
                
                backupManager.restoreBackup(backupPath)
                
                showBackupNotification("Restore completed successfully", false)
                
            } catch (e: Exception) {
                showBackupNotification("Restore failed: ${e.message}", false)
            } finally {
                stopSelf()
            }
        }
    }
    
    private fun showBackupNotification(message: String, ongoing: Boolean) {
        val notification = NotificationCompat.Builder(this, NotificationService.CHANNEL_ID_REMINDERS)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle("Backup & Restore")
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setOngoing(ongoing)
            .setAutoCancel(!ongoing)
            .build()
        
        startForeground(NOTIFICATION_ID_BACKUP, notification)
        
        if (!ongoing) {
            stopForeground(false)
        }
    }
    
    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
    }
}