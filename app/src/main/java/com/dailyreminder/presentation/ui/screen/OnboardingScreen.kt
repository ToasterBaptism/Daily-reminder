package com.dailyreminder.presentation.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

data class OnboardingPage(
    val title: String,
    val description: String,
    val icon: ImageVector
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OnboardingScreen(
    onComplete: () -> Unit
) {
    val pages = listOf(
        OnboardingPage(
            title = "Welcome to Daily Reminder",
            description = "Your personal offline planner for meals, tasks, and events. Everything stays on your device - no internet required!",
            icon = Icons.Default.Smartphone
        ),
        OnboardingPage(
            title = "Plan Your Meals",
            description = "Schedule breakfast, lunch, dinner, and snacks. Add recipes, ingredients, and cooking times for perfect meal planning.",
            icon = Icons.Default.Restaurant
        ),
        OnboardingPage(
            title = "Manage Your Tasks",
            description = "Create tasks with priorities, due dates, and progress tracking. Stay organized with categories and subtasks.",
            icon = Icons.Default.Task
        ),
        OnboardingPage(
            title = "Never Miss Events",
            description = "Schedule appointments, meetings, and personal events with customizable notifications and reminders.",
            icon = Icons.Default.Event
        ),
        OnboardingPage(
            title = "Stay Notified",
            description = "Get timely reminders for all your activities. Customize notification sounds, timing, and priority levels.",
            icon = Icons.Default.Notifications
        ),
        OnboardingPage(
            title = "Backup & Privacy",
            description = "Your data is encrypted and stored locally. Create backups anytime and restore when needed - all offline!",
            icon = Icons.Default.Security
        )
    )
    
    val pagerState = rememberPagerState(pageCount = { pages.size })
    val scope = rememberCoroutineScope()
    
    Scaffold(
        bottomBar = {
            Surface(
                color = MaterialTheme.colorScheme.surface,
                shadowElevation = 8.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Skip button
                    if (pagerState.currentPage < pages.size - 1) {
                        TextButton(
                            onClick = onComplete,
                            modifier = Modifier.semantics {
                                contentDescription = "Skip onboarding"
                            }
                        ) {
                            Text("Skip")
                        }
                    } else {
                        Spacer(modifier = Modifier.width(64.dp))
                    }
                    
                    // Page indicators
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        repeat(pages.size) { index ->
                            Box(
                                modifier = Modifier
                                    .size(if (index == pagerState.currentPage) 12.dp else 8.dp)
                                    .background(
                                        color = if (index == pagerState.currentPage) 
                                            MaterialTheme.colorScheme.primary 
                                        else 
                                            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
                                        shape = androidx.compose.foundation.shape.CircleShape
                                    )
                            )
                        }
                    }
                    
                    // Next/Get Started button
                    if (pagerState.currentPage < pages.size - 1) {
                        Button(
                            onClick = {
                                scope.launch {
                                    pagerState.animateScrollToPage(pagerState.currentPage + 1)
                                }
                            },
                            modifier = Modifier.semantics {
                                contentDescription = "Next page"
                            }
                        ) {
                            Text("Next")
                        }
                    } else {
                        Button(
                            onClick = onComplete,
                            modifier = Modifier.semantics {
                                contentDescription = "Get started with Daily Reminder"
                            }
                        ) {
                            Text("Get Started")
                        }
                    }
                }
            }
        }
    ) { paddingValues ->
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) { page ->
            OnboardingPageContent(
                page = pages[page],
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@Composable
private fun OnboardingPageContent(
    page: OnboardingPage,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Icon
        Card(
            modifier = Modifier.size(120.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = page.icon,
                    contentDescription = null,
                    modifier = Modifier.size(64.dp),
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        
        // Title
        Text(
            text = page.title,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurface
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Description
        Text(
            text = page.description,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            lineHeight = MaterialTheme.typography.bodyLarge.lineHeight * 1.2
        )
    }
}