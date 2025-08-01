package com.dailyreminder.presentation.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.dailyreminder.data.model.Event
import com.dailyreminder.data.util.DateUtils
import com.dailyreminder.presentation.ui.theme.eventColor
import com.dailyreminder.presentation.viewmodel.CalendarViewModel
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate

@Composable
fun CalendarView(
    selectedDate: LocalDate,
    viewType: CalendarViewModel.CalendarViewType,
    events: List<Event>,
    onDateSelected: (LocalDate) -> Unit,
    onViewTypeChanged: (CalendarViewModel.CalendarViewType) -> Unit,
    onNavigateToNextMonth: () -> Unit,
    onNavigateToPreviousMonth: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(16.dp)
    ) {
        // Header with navigation and view type selector
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Navigation buttons
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onNavigateToPreviousMonth,
                    modifier = Modifier.semantics {
                        contentDescription = "Previous month"
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.ChevronLeft,
                        contentDescription = "Previous"
                    )
                }
                
                Text(
                    text = "${selectedDate.month.name} ${selectedDate.year}",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                
                IconButton(
                    onClick = onNavigateToNextMonth,
                    modifier = Modifier.semantics {
                        contentDescription = "Next month"
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.ChevronRight,
                        contentDescription = "Next"
                    )
                }
            }
            
            // View type selector
            Row {
                CalendarViewModel.CalendarViewType.values().forEach { type ->
                    FilterChip(
                        selected = viewType == type,
                        onClick = { onViewTypeChanged(type) },
                        label = { 
                            Text(
                                text = when (type) {
                                    CalendarViewModel.CalendarViewType.MONTH -> "M"
                                    CalendarViewModel.CalendarViewType.WEEK -> "W"
                                    CalendarViewModel.CalendarViewType.DAY -> "D"
                                },
                                style = MaterialTheme.typography.labelSmall
                            )
                        },
                        modifier = Modifier
                            .padding(horizontal = 2.dp)
                            .semantics {
                                contentDescription = when (type) {
                                    CalendarViewModel.CalendarViewType.MONTH -> "Month view"
                                    CalendarViewModel.CalendarViewType.WEEK -> "Week view"
                                    CalendarViewModel.CalendarViewType.DAY -> "Day view"
                                }
                            }
                    )
                }
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        when (viewType) {
            CalendarViewModel.CalendarViewType.MONTH -> {
                MonthView(
                    selectedDate = selectedDate,
                    events = events,
                    onDateSelected = onDateSelected
                )
            }
            CalendarViewModel.CalendarViewType.WEEK -> {
                WeekView(
                    selectedDate = selectedDate,
                    events = events,
                    onDateSelected = onDateSelected
                )
            }
            CalendarViewModel.CalendarViewType.DAY -> {
                DayView(
                    selectedDate = selectedDate,
                    events = events
                )
            }
        }
    }
}

@Composable
private fun MonthView(
    selectedDate: LocalDate,
    events: List<Event>,
    onDateSelected: (LocalDate) -> Unit
) {
    val monthDays = DateUtils.getMonthDays(selectedDate.year, selectedDate.month)
    val startOfMonth = monthDays.first()
    val startDayOfWeek = startOfMonth.dayOfWeek.ordinal // Monday = 0
    
    // Add empty cells for days before the first day of the month
    val calendarDays = List(startDayOfWeek) { null } + monthDays
    
    Column {
        // Day of week headers
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            DayOfWeek.values().forEach { dayOfWeek ->
                Text(
                    text = dayOfWeek.name.take(3),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.weight(1f)
                )
            }
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // Calendar grid
        LazyVerticalGrid(
            columns = GridCells.Fixed(7),
            verticalArrangement = Arrangement.spacedBy(4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.height(240.dp)
        ) {
            items(calendarDays) { date ->
                CalendarDayCell(
                    date = date,
                    isSelected = date == selectedDate,
                    isToday = date?.let { DateUtils.isToday(it) } ?: false,
                    hasEvents = date?.let { d -> events.any { DateUtils.formatDate(it.startDateTime.date) == DateUtils.formatDate(d) } } ?: false,
                    onClick = { date?.let(onDateSelected) }
                )
            }
        }
    }
}

@Composable
private fun WeekView(
    selectedDate: LocalDate,
    events: List<Event>,
    onDateSelected: (LocalDate) -> Unit
) {
    val startOfWeek = DateUtils.startOfWeek(selectedDate)
    val weekDays = DateUtils.getWeekDays(startOfWeek)
    
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            weekDays.forEach { date ->
                CalendarDayCell(
                    date = date,
                    isSelected = date == selectedDate,
                    isToday = DateUtils.isToday(date),
                    hasEvents = events.any { DateUtils.formatDate(it.startDateTime.date) == DateUtils.formatDate(date) },
                    onClick = { onDateSelected(date) },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun DayView(
    selectedDate: LocalDate,
    events: List<Event>
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = DateUtils.formatDate(selectedDate),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            
            Text(
                text = selectedDate.dayOfWeek.name,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            
            if (DateUtils.isToday(selectedDate)) {
                Text(
                    text = "Today",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            
            if (events.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "${events.size} event${if (events.size != 1) "s" else ""}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.eventColor()
                )
            }
        }
    }
}

@Composable
private fun CalendarDayCell(
    date: LocalDate?,
    isSelected: Boolean,
    isToday: Boolean,
    hasEvents: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .aspectRatio(1f)
            .clip(CircleShape)
            .background(
                when {
                    isSelected -> MaterialTheme.colorScheme.primary
                    isToday -> MaterialTheme.colorScheme.primaryContainer
                    else -> Color.Transparent
                }
            )
            .clickable(enabled = date != null) { onClick() }
            .semantics {
                if (date != null) {
                    contentDescription = buildString {
                        append(DateUtils.formatDate(date))
                        if (isToday) append(", Today")
                        if (isSelected) append(", Selected")
                        if (hasEvents) append(", Has events")
                    }
                }
            },
        contentAlignment = Alignment.Center
    ) {
        if (date != null) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = date.dayOfMonth.toString(),
                    style = MaterialTheme.typography.bodyMedium,
                    color = when {
                        isSelected -> MaterialTheme.colorScheme.onPrimary
                        isToday -> MaterialTheme.colorScheme.onPrimaryContainer
                        DateUtils.isWeekend(date) -> MaterialTheme.colorScheme.error
                        else -> MaterialTheme.colorScheme.onSurface
                    },
                    fontWeight = if (isToday || isSelected) FontWeight.Bold else FontWeight.Normal
                )
                
                if (hasEvents) {
                    Box(
                        modifier = Modifier
                            .size(4.dp)
                            .clip(CircleShape)
                            .background(
                                if (isSelected) MaterialTheme.colorScheme.onPrimary
                                else MaterialTheme.colorScheme.eventColor()
                            )
                    )
                }
            }
        }
    }
}