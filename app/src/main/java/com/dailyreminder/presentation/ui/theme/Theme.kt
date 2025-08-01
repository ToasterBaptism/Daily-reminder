package com.dailyreminder.presentation.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = DarkPrimary,
    onPrimary = DarkOnPrimary,
    primaryContainer = DarkPrimaryContainer,
    onPrimaryContainer = DarkOnPrimaryContainer,
    secondary = DarkSecondary,
    onSecondary = DarkOnSecondary,
    secondaryContainer = DarkSecondaryContainer,
    onSecondaryContainer = DarkOnSecondaryContainer,
    tertiary = DarkTertiary,
    onTertiary = DarkOnTertiary,
    tertiaryContainer = DarkTertiaryContainer,
    onTertiaryContainer = DarkOnTertiaryContainer,
    error = DarkError,
    onError = DarkOnError,
    errorContainer = DarkErrorContainer,
    onErrorContainer = DarkOnErrorContainer,
    background = DarkBackground,
    onBackground = DarkOnBackground,
    surface = DarkSurface,
    onSurface = DarkOnSurface,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = DarkOnSurfaceVariant,
    outline = DarkOutline,
    outlineVariant = DarkOutlineVariant,
    inverseSurface = DarkInverseSurface,
    inverseOnSurface = DarkInverseOnSurface,
    inversePrimary = DarkInversePrimary,
)

private val LightColorScheme = lightColorScheme(
    primary = Primary,
    onPrimary = OnPrimary,
    primaryContainer = PrimaryContainer,
    onPrimaryContainer = OnPrimaryContainer,
    secondary = Secondary,
    onSecondary = OnSecondary,
    secondaryContainer = SecondaryContainer,
    onSecondaryContainer = OnSecondaryContainer,
    tertiary = Tertiary,
    onTertiary = OnTertiary,
    tertiaryContainer = TertiaryContainer,
    onTertiaryContainer = OnTertiaryContainer,
    error = Error,
    onError = OnError,
    errorContainer = ErrorContainer,
    onErrorContainer = OnErrorContainer,
    background = Background,
    onBackground = OnBackground,
    surface = Surface,
    onSurface = OnSurface,
    surfaceVariant = SurfaceVariant,
    onSurfaceVariant = OnSurfaceVariant,
    outline = Outline,
    outlineVariant = OutlineVariant,
    inverseSurface = InverseSurface,
    inverseOnSurface = InverseOnSurface,
    inversePrimary = InversePrimary,
)

private val HighContrastLightColorScheme = lightColorScheme(
    primary = HighContrastPrimary,
    onPrimary = HighContrastOnPrimary,
    primaryContainer = HighContrastPrimary,
    onPrimaryContainer = HighContrastOnPrimary,
    secondary = HighContrastPrimary,
    onSecondary = HighContrastOnPrimary,
    secondaryContainer = HighContrastPrimary,
    onSecondaryContainer = HighContrastOnPrimary,
    tertiary = HighContrastPrimary,
    onTertiary = HighContrastOnPrimary,
    tertiaryContainer = HighContrastPrimary,
    onTertiaryContainer = HighContrastOnPrimary,
    error = HighContrastError,
    onError = HighContrastOnError,
    errorContainer = HighContrastError,
    onErrorContainer = HighContrastOnError,
    background = HighContrastBackground,
    onBackground = HighContrastOnBackground,
    surface = HighContrastSurface,
    onSurface = HighContrastOnSurface,
    surfaceVariant = HighContrastSurface,
    onSurfaceVariant = HighContrastOnSurface,
    outline = HighContrastOnBackground,
    outlineVariant = HighContrastOnBackground,
    inverseSurface = HighContrastOnBackground,
    inverseOnSurface = HighContrastBackground,
    inversePrimary = HighContrastBackground,
)

@Composable
fun DailyReminderTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Disabled for consistent offline experience
    highContrast: Boolean = false,
    largeText: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        highContrast -> HighContrastLightColorScheme
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    
    val typography = if (largeText) AccessibilityTypography else Typography
    
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = typography,
        content = content
    )
}

// Extension functions for theme-aware colors
@Composable
fun ColorScheme.eventColor() = if (isSystemInDarkTheme()) EventColor else EventColor

@Composable
fun ColorScheme.mealColor() = if (isSystemInDarkTheme()) MealColor else MealColor

@Composable
fun ColorScheme.taskColor() = if (isSystemInDarkTheme()) TaskColor else TaskColor

@Composable
fun ColorScheme.notificationColor() = if (isSystemInDarkTheme()) NotificationColor else NotificationColor

@Composable
fun ColorScheme.priorityColor(priority: com.dailyreminder.data.model.Priority) = when (priority) {
    com.dailyreminder.data.model.Priority.LOW -> PriorityLow
    com.dailyreminder.data.model.Priority.MEDIUM -> PriorityMedium
    com.dailyreminder.data.model.Priority.HIGH -> PriorityHigh
    com.dailyreminder.data.model.Priority.URGENT -> PriorityUrgent
}

@Composable
fun ColorScheme.categoryColor(category: com.dailyreminder.data.model.EventCategory) = when (category) {
    com.dailyreminder.data.model.EventCategory.WORK -> WorkColor
    com.dailyreminder.data.model.EventCategory.PERSONAL -> PersonalColor
    com.dailyreminder.data.model.EventCategory.HEALTH -> HealthColor
    com.dailyreminder.data.model.EventCategory.SOCIAL -> SocialColor
    com.dailyreminder.data.model.EventCategory.APPOINTMENT -> AppointmentColor
    com.dailyreminder.data.model.EventCategory.OTHER -> outline
}