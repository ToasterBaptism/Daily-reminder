package com.dailyreminder.presentation.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Today
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dailyreminder.R
import com.dailyreminder.data.util.DateUtils
import com.dailyreminder.presentation.ui.component.CalendarView
import com.dailyreminder.presentation.ui.component.EventCard
import com.dailyreminder.presentation.ui.component.MealCard
import com.dailyreminder.presentation.ui.component.TaskItem
import com.dailyreminder.presentation.viewmodel.CalendarViewModel
import kotlinx.datetime.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarScreen(
    onNavigateToEventDetail: (Long) -> Unit,
    onNavigateToMealDetail: (Long) -> Unit,
    onNavigateToTaskDetail: (Long) -> Unit,
    onCreateEvent: () -> Unit,
    onCreateMeal: () -> Unit,
    onCreateTask: () -> Unit,
    viewModel: CalendarViewModel = hiltViewModel()
) {
    val selectedDate by viewModel.selectedDate.collectAsStateWithLifecycle()
    val calendarViewType by viewModel.calendarViewType.collectAsStateWithLifecycle()
    val itemsForSelectedDate by viewModel.itemsForSelectedDate.collectAsStateWithLifecycle()
    val eventsForCurrentMonth by viewModel.eventsForCurrentMonth.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val error by viewModel.error.collectAsStateWithLifecycle()
    
    val context = LocalContext.current
    var showCreateDialog by remember { mutableStateOf(false) }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        text = when (calendarViewType) {
                            CalendarViewModel.CalendarViewType.MONTH -> DateUtils.formatDate(selectedDate)
                            CalendarViewModel.CalendarViewType.WEEK -> "Week of ${DateUtils.formatDate(selectedDate)}"
                            CalendarViewModel.CalendarViewType.DAY -> DateUtils.formatDate(selectedDate)
                        },
                        modifier = Modifier.semantics {
                            contentDescription = "Current date: ${DateUtils.formatDate(selectedDate)}"
                        }
                    )
                },
                actions = {
                    IconButton(
                        onClick = { viewModel.selectToday() },
                        modifier = Modifier.semantics {
                            contentDescription = "Go to today"
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Today,
                            contentDescription = "Today"
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showCreateDialog = true },
                modifier = Modifier.semantics {
                    contentDescription = "Create new item"
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add"
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Calendar View
            CalendarView(
                selectedDate = selectedDate,
                viewType = calendarViewType,
                events = eventsForCurrentMonth,
                onDateSelected = viewModel::selectDate,
                onViewTypeChanged = viewModel::setCalendarViewType,
                onNavigateToNextMonth = viewModel::navigateToNextMonth,
                onNavigateToPreviousMonth = viewModel::navigateToPreviousMonth,
                modifier = Modifier.fillMaxWidth()
            )
            
            Divider()
            
            // Items for selected date
            if (isLoading) {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.semantics {
                            contentDescription = "Loading calendar items"
                        }
                    )
                }
            } else if (error != null) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Error",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onErrorContainer
                        )
                        Text(
                            text = error,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onErrorContainer
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(
                            onClick = { 
                                viewModel.clearError()
                                viewModel.refreshData()
                            }
                        ) {
                            Text("Retry")
                        }
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (!itemsForSelectedDate.hasItems) {
                        item {
                            Card(
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(24.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = "No items for ${DateUtils.formatDate(selectedDate)}",
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = "Tap the + button to add events, meals, or tasks",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    } else {
                        // Events
                        if (itemsForSelectedDate.events.isNotEmpty()) {
                            item {
                                Text(
                                    text = "Events (${itemsForSelectedDate.events.size})",
                                    style = MaterialTheme.typography.titleMedium,
                                    modifier = Modifier.padding(vertical = 8.dp)
                                )
                            }
                            items(itemsForSelectedDate.events) { event ->
                                EventCard(
                                    event = event,
                                    onClick = { onNavigateToEventDetail(event.id) },
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }
                        
                        // Meals
                        if (itemsForSelectedDate.meals.isNotEmpty()) {
                            item {
                                Text(
                                    text = "Meals (${itemsForSelectedDate.meals.size})",
                                    style = MaterialTheme.typography.titleMedium,
                                    modifier = Modifier.padding(vertical = 8.dp)
                                )
                            }
                            items(itemsForSelectedDate.meals) { meal ->
                                MealCard(
                                    meal = meal,
                                    onClick = { onNavigateToMealDetail(meal.id) },
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }
                        
                        // Tasks
                        if (itemsForSelectedDate.tasks.isNotEmpty()) {
                            item {
                                Text(
                                    text = "Tasks (${itemsForSelectedDate.tasks.size})",
                                    style = MaterialTheme.typography.titleMedium,
                                    modifier = Modifier.padding(vertical = 8.dp)
                                )
                            }
                            items(itemsForSelectedDate.tasks) { task ->
                                TaskItem(
                                    task = task,
                                    onClick = { onNavigateToTaskDetail(task.id) },
                                    onToggleComplete = { /* TODO: Implement */ },
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }
                    }
                }
            }
        }
    }
    
    // Create Item Dialog
    if (showCreateDialog) {
        AlertDialog(
            onDismissRequest = { showCreateDialog = false },
            title = { Text("Create New Item") },
            text = { Text("What would you like to create?") },
            confirmButton = {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    TextButton(
                        onClick = {
                            showCreateDialog = false
                            onCreateEvent()
                        }
                    ) {
                        Text("Event")
                    }
                    TextButton(
                        onClick = {
                            showCreateDialog = false
                            onCreateMeal()
                        }
                    ) {
                        Text("Meal")
                    }
                    TextButton(
                        onClick = {
                            showCreateDialog = false
                            onCreateTask()
                        }
                    ) {
                        Text("Task")
                    }
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showCreateDialog = false }
                ) {
                    Text("Cancel")
                }
            }
        )
    }
}