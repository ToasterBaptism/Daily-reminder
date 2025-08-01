package com.dailyreminder.presentation.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.dailyreminder.R
import com.dailyreminder.presentation.ui.screen.*

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    object Calendar : Screen("calendar", "Calendar", Icons.Default.CalendarMonth)
    object Meals : Screen("meals", "Meals", Icons.Default.Restaurant)
    object Tasks : Screen("tasks", "Tasks", Icons.Default.Task)
    object Notifications : Screen("notifications", "Notifications", Icons.Default.Notifications)
    object Settings : Screen("settings", "Settings", Icons.Default.Settings)
    
    // Detail screens
    object EventDetail : Screen("event_detail/{eventId}", "Event Details", Icons.Default.Event)
    object MealDetail : Screen("meal_detail/{mealId}", "Meal Details", Icons.Default.Restaurant)
    object TaskDetail : Screen("task_detail/{taskId}", "Task Details", Icons.Default.Task)
    object CreateEvent : Screen("create_event", "Create Event", Icons.Default.Add)
    object CreateMeal : Screen("create_meal", "Create Meal", Icons.Default.Add)
    object CreateTask : Screen("create_task", "Create Task", Icons.Default.Add)
    object Backup : Screen("backup", "Backup & Restore", Icons.Default.Backup)
    object Onboarding : Screen("onboarding", "Welcome", Icons.Default.Info)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationGraph(
    isFirstLaunch: Boolean,
    onFirstLaunchCompleted: () -> Unit,
    onThemeChanged: (Boolean) -> Unit,
    onHighContrastChanged: (Boolean) -> Unit,
    onLargeTextChanged: (Boolean) -> Unit,
    navController: NavHostController = rememberNavController()
) {
    val bottomNavItems = listOf(
        Screen.Calendar,
        Screen.Meals,
        Screen.Tasks,
        Screen.Notifications,
        Screen.Settings
    )
    
    Scaffold(
        bottomBar = {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentDestination = navBackStackEntry?.destination
            
            // Only show bottom navigation on main screens
            if (currentDestination?.route in bottomNavItems.map { it.route }) {
                NavigationBar {
                    bottomNavItems.forEach { screen ->
                        NavigationBarItem(
                            icon = { 
                                Icon(
                                    imageVector = screen.icon,
                                    contentDescription = screen.title
                                )
                            },
                            label = { Text(screen.title) },
                            selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                            onClick = {
                                navController.navigate(screen.route) {
                                    // Pop up to the start destination of the graph to
                                    // avoid building up a large stack of destinations
                                    // on the back stack as users select items
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    // Avoid multiple copies of the same destination when
                                    // reselecting the same item
                                    launchSingleTop = true
                                    // Restore state when reselecting a previously selected item
                                    restoreState = true
                                }
                            }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = if (isFirstLaunch) Screen.Onboarding.route else Screen.Calendar.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            // Onboarding
            composable(Screen.Onboarding.route) {
                OnboardingScreen(
                    onComplete = {
                        onFirstLaunchCompleted()
                        navController.navigate(Screen.Calendar.route) {
                            popUpTo(Screen.Onboarding.route) { inclusive = true }
                        }
                    }
                )
            }
            
            // Main screens
            composable(Screen.Calendar.route) {
                CalendarScreen(
                    onNavigateToEventDetail = { eventId ->
                        navController.navigate("event_detail/$eventId")
                    },
                    onNavigateToMealDetail = { mealId ->
                        navController.navigate("meal_detail/$mealId")
                    },
                    onNavigateToTaskDetail = { taskId ->
                        navController.navigate("task_detail/$taskId")
                    },
                    onCreateEvent = {
                        navController.navigate(Screen.CreateEvent.route)
                    },
                    onCreateMeal = {
                        navController.navigate(Screen.CreateMeal.route)
                    },
                    onCreateTask = {
                        navController.navigate(Screen.CreateTask.route)
                    }
                )
            }
            
            composable(Screen.Meals.route) {
                MealPlanningScreen(
                    onNavigateToMealDetail = { mealId ->
                        navController.navigate("meal_detail/$mealId")
                    },
                    onCreateMeal = {
                        navController.navigate(Screen.CreateMeal.route)
                    }
                )
            }
            
            composable(Screen.Tasks.route) {
                TaskListScreen(
                    onNavigateToTaskDetail = { taskId ->
                        navController.navigate("task_detail/$taskId")
                    },
                    onCreateTask = {
                        navController.navigate(Screen.CreateTask.route)
                    }
                )
            }
            
            composable(Screen.Notifications.route) {
                NotificationScreen()
            }
            
            composable(Screen.Settings.route) {
                SettingsScreen(
                    onNavigateToBackup = {
                        navController.navigate(Screen.Backup.route)
                    },
                    onThemeChanged = onThemeChanged,
                    onHighContrastChanged = onHighContrastChanged,
                    onLargeTextChanged = onLargeTextChanged
                )
            }
            
            // Detail screens
            composable("event_detail/{eventId}") { backStackEntry ->
                val eventId = backStackEntry.arguments?.getString("eventId")?.toLongOrNull() ?: 0L
                EventDetailScreen(
                    eventId = eventId,
                    onNavigateBack = { navController.popBackStack() }
                )
            }
            
            composable("meal_detail/{mealId}") { backStackEntry ->
                val mealId = backStackEntry.arguments?.getString("mealId")?.toLongOrNull() ?: 0L
                MealDetailScreen(
                    mealId = mealId,
                    onNavigateBack = { navController.popBackStack() }
                )
            }
            
            composable("task_detail/{taskId}") { backStackEntry ->
                val taskId = backStackEntry.arguments?.getString("taskId")?.toLongOrNull() ?: 0L
                TaskDetailScreen(
                    taskId = taskId,
                    onNavigateBack = { navController.popBackStack() }
                )
            }
            
            // Create screens
            composable(Screen.CreateEvent.route) {
                CreateEventScreen(
                    onNavigateBack = { navController.popBackStack() },
                    onEventCreated = { navController.popBackStack() }
                )
            }
            
            composable(Screen.CreateMeal.route) {
                CreateMealScreen(
                    onNavigateBack = { navController.popBackStack() },
                    onMealCreated = { navController.popBackStack() }
                )
            }
            
            composable(Screen.CreateTask.route) {
                CreateTaskScreen(
                    onNavigateBack = { navController.popBackStack() },
                    onTaskCreated = { navController.popBackStack() }
                )
            }
            
            // Backup screen
            composable(Screen.Backup.route) {
                BackupScreen(
                    onNavigateBack = { navController.popBackStack() }
                )
            }
        }
    }
}