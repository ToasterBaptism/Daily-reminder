package com.dailyreminder.presentation.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.dailyreminder.presentation.ui.screen.CalendarScreen
import com.dailyreminder.presentation.ui.screen.MealPlanningScreen
import com.dailyreminder.presentation.ui.screen.OnboardingScreen
import com.dailyreminder.presentation.ui.screen.SettingsScreen
import com.dailyreminder.presentation.ui.screen.TaskListScreen

@Composable
fun NavigationGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Onboarding.route,
        modifier = modifier
    ) {
        composable(Screen.Onboarding.route) {
            OnboardingScreen(
                onNavigateToCalendar = {
                    navController.navigate(Screen.Calendar.route) {
                        popUpTo(Screen.Onboarding.route) { inclusive = true }
                    }
                }
            )
        }
        
        composable(Screen.Calendar.route) {
            CalendarScreen(
                onNavigateToMeals = { navController.navigate(Screen.MealPlanning.route) },
                onNavigateToTasks = { navController.navigate(Screen.TaskList.route) },
                onNavigateToSettings = { navController.navigate(Screen.Settings.route) }
            )
        }
        
        composable(Screen.MealPlanning.route) {
            MealPlanningScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToCalendar = { navController.navigate(Screen.Calendar.route) }
            )
        }
        
        composable(Screen.TaskList.route) {
            TaskListScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToCalendar = { navController.navigate(Screen.Calendar.route) }
            )
        }
        
        composable(Screen.Settings.route) {
            SettingsScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}

sealed class Screen(val route: String) {
    object Onboarding : Screen("onboarding")
    object Calendar : Screen("calendar")
    object MealPlanning : Screen("meal_planning")
    object TaskList : Screen("task_list")
    object Settings : Screen("settings")
}