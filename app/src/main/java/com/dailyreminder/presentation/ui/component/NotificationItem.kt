package com.dailyreminder.presentation.ui.component

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.dailyreminder.data.model.Notification
import com.dailyreminder.data.model.NotificationType
import com.dailyreminder.data.util.DateUtils

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationItem(
    notification: Notification,
    onClick: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = if (notification.isRead) 
                MaterialTheme.colorScheme.surfaceVariant 
            else 
                MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            // Notification type icon
            Icon(
                imageVector = getNotificationTypeIcon(notification.type),
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = getNotificationTypeColor(notification.type)
            )
            
            Spacer(modifier = Modifier.width(12.dp))
            
            Column(
                modifier = Modifier.weight(1f)
            ) {
                // Title
                Text(
                    text = notification.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = if (notification.isRead) FontWeight.Normal else FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                
                // Message
                if (notification.message.isNotBlank()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = notification.message,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                
                // Timestamp
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = DateUtils.formatDateTime(notification.scheduledDateTime),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            // Dismiss button
            IconButton(
                onClick = onDismiss,
                modifier = Modifier.size(24.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Dismiss",
                    modifier = Modifier.size(16.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun getNotificationTypeIcon(type: NotificationType): ImageVector {
    return when (type) {
        NotificationType.EVENT_REMINDER -> Icons.Default.Event
        NotificationType.MEAL_REMINDER -> Icons.Default.Restaurant
        NotificationType.TASK_REMINDER -> Icons.Default.Task
        NotificationType.BACKUP_REMINDER -> Icons.Default.Backup
        NotificationType.SYSTEM -> Icons.Default.Info
    }
}

@Composable
private fun getNotificationTypeColor(type: NotificationType): androidx.compose.ui.graphics.Color {
    return when (type) {
        NotificationType.EVENT_REMINDER -> MaterialTheme.colorScheme.primary
        NotificationType.MEAL_REMINDER -> MaterialTheme.colorScheme.secondary
        NotificationType.TASK_REMINDER -> MaterialTheme.colorScheme.tertiary
        NotificationType.BACKUP_REMINDER -> MaterialTheme.colorScheme.outline
        NotificationType.SYSTEM -> MaterialTheme.colorScheme.onSurfaceVariant
    }
}