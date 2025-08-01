package com.dailyreminder.presentation.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MealPlanningScreen(
    onNavigateToMealDetail: (Long) -> Unit,
    onCreateMeal: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Meal Planning") }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onCreateMeal
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Meal"
                )
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Meal Planning",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Coming soon! Plan your meals here.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}