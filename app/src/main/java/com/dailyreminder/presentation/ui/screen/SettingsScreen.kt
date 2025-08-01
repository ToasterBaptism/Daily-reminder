package com.dailyreminder.presentation.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onNavigateToBackup: () -> Unit,
    onThemeChanged: (Boolean) -> Unit,
    onHighContrastChanged: (Boolean) -> Unit,
    onLargeTextChanged: (Boolean) -> Unit
) {
    var darkTheme by remember { mutableStateOf(false) }
    var highContrast by remember { mutableStateOf(false) }
    var largeText by remember { mutableStateOf(false) }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings") }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                SettingsSection(title = "Appearance") {
                    SettingsSwitchItem(
                        title = "Dark Theme",
                        description = "Use dark colors throughout the app",
                        icon = Icons.Default.DarkMode,
                        checked = darkTheme,
                        onCheckedChange = { 
                            darkTheme = it
                            onThemeChanged(it)
                        }
                    )
                    
                    SettingsSwitchItem(
                        title = "High Contrast",
                        description = "Increase contrast for better visibility",
                        icon = Icons.Default.Contrast,
                        checked = highContrast,
                        onCheckedChange = { 
                            highContrast = it
                            onHighContrastChanged(it)
                        }
                    )
                    
                    SettingsSwitchItem(
                        title = "Large Text",
                        description = "Use larger text sizes for better readability",
                        icon = Icons.Default.FormatSize,
                        checked = largeText,
                        onCheckedChange = { 
                            largeText = it
                            onLargeTextChanged(it)
                        }
                    )
                }
            }
            
            item {
                SettingsSection(title = "Data") {
                    SettingsClickableItem(
                        title = "Backup & Restore",
                        description = "Manage your data backups",
                        icon = Icons.Default.Backup,
                        onClick = onNavigateToBackup
                    )
                }
            }
            
            item {
                SettingsSection(title = "About") {
                    SettingsClickableItem(
                        title = "Version",
                        description = "Daily Reminder v1.0.0",
                        icon = Icons.Default.Info,
                        onClick = { /* TODO: Show version info */ }
                    )
                    
                    SettingsClickableItem(
                        title = "Privacy Policy",
                        description = "Your data stays on your device",
                        icon = Icons.Default.Privacy,
                        onClick = { /* TODO: Show privacy policy */ }
                    )
                }
            }
        }
    }
}

@Composable
private fun SettingsSection(
    title: String,
    content: @Composable () -> Unit
) {
    Column {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            content()
        }
    }
}

@Composable
private fun SettingsSwitchItem(
    title: String,
    description: String,
    icon: ImageVector,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(24.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
        
        Spacer(modifier = Modifier.width(16.dp))
        
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
    }
}

@Composable
private fun SettingsClickableItem(
    title: String,
    description: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(24.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
        
        Spacer(modifier = Modifier.width(16.dp))
        
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        
        Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}