package com.dailyreminder

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.dailyreminder.presentation.ui.navigation.NavigationGraph
import com.dailyreminder.presentation.ui.theme.DailyReminderTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

private val android.content.Context.dataStore by preferencesDataStore("settings")

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    
    companion object {
        private val DARK_THEME_KEY = booleanPreferencesKey("dark_theme")
        private val HIGH_CONTRAST_KEY = booleanPreferencesKey("high_contrast")
        private val LARGE_TEXT_KEY = booleanPreferencesKey("large_text")
        private val FIRST_LAUNCH_KEY = booleanPreferencesKey("first_launch")
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        // Install splash screen
        installSplashScreen()
        
        super.onCreate(savedInstanceState)
        
        // Enable edge-to-edge display
        enableEdgeToEdge()
        
        setContent {
            val context = LocalContext.current
            
            // Collect theme preferences
            val darkTheme by context.dataStore.data
                .map { preferences -> preferences[DARK_THEME_KEY] ?: false }
                .collectAsStateWithLifecycle(initialValue = false)
            
            val highContrast by context.dataStore.data
                .map { preferences -> preferences[HIGH_CONTRAST_KEY] ?: false }
                .collectAsStateWithLifecycle(initialValue = false)
            
            val largeText by context.dataStore.data
                .map { preferences -> preferences[LARGE_TEXT_KEY] ?: false }
                .collectAsStateWithLifecycle(initialValue = false)
            
            val isFirstLaunch by context.dataStore.data
                .map { preferences -> preferences[FIRST_LAUNCH_KEY] ?: true }
                .collectAsStateWithLifecycle(initialValue = true)
            
            DailyReminderTheme(
                darkTheme = darkTheme,
                highContrast = highContrast,
                largeText = largeText
            ) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavigationGraph(
                        isFirstLaunch = isFirstLaunch,
                        onFirstLaunchCompleted = {
                            // Mark first launch as completed
                            lifecycleScope.launch {
                                context.dataStore.edit { preferences ->
                                    preferences[FIRST_LAUNCH_KEY] = false
                                }
                            }
                        },
                        onThemeChanged = { isDark ->
                            lifecycleScope.launch {
                                context.dataStore.edit { preferences ->
                                    preferences[DARK_THEME_KEY] = isDark
                                }
                            }
                        },
                        onHighContrastChanged = { isHighContrast ->
                            lifecycleScope.launch {
                                context.dataStore.edit { preferences ->
                                    preferences[HIGH_CONTRAST_KEY] = isHighContrast
                                }
                            }
                        },
                        onLargeTextChanged = { isLargeText ->
                            lifecycleScope.launch {
                                context.dataStore.edit { preferences ->
                                    preferences[LARGE_TEXT_KEY] = isLargeText
                                }
                            }
                        }
                    )
                }
            }
        }
    }
}