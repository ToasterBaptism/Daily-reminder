package com.dailyreminder.presentation.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.dailyreminder.data.model.Event
import com.dailyreminder.data.util.DateUtils
import com.dailyreminder.presentation.ui.theme.SelectedDateColor
import com.dailyreminder.presentation.ui.theme.TodayColor
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month
import kotlinx.datetime.plus

@Composable
fun CalendarView(
    selectedDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit,
    events: List<Event>,
    modifier: Modifier = Modifier
) {
    var currentMonth by remember { mutableStateOf(selectedDate) }
    val today = DateUtils.getCurrentDate()
    
    Column(modifier = modifier) {
        // Month header with navigation
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { 
                    currentMonth = currentMonth.plus(-1, DateTimeUnit.MONTH)
                }
            ) {
                Icon(Icons.Default.KeyboardArrowLeft, contentDescription = "Previous month")
            }
            
            Text(
                text = "${currentMonth.month.name} ${currentMonth.year}",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            
            IconButton(
                onClick = { 
                    currentMonth = currentMonth.plus(1, DateTimeUnit.MONTH)
                }
            ) {
                Icon(Icons.Default.KeyboardArrowRight, contentDescription = "Next month")
            }
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // Days of week header
        Row(modifier = Modifier.fillMaxWidth()) {
            val daysOfWeek = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
            daysOfWeek.forEach { day ->
                Text(
                    text = day,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // Calendar grid
        val startOfMonth = DateUtils.getStartOfMonth(currentMonth)
        val endOfMonth = DateUtils.getEndOfMonth(currentMonth)
        val startOfCalendar = DateUtils.getStartOfWeek(startOfMonth)
        val daysToShow = generateCalendarDays(startOfCalendar, endOfMonth)
        
        LazyVerticalGrid(
            columns = GridCells.Fixed(7),
            verticalArrangement = Arrangement.spacedBy(4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            items(daysToShow) { date ->
                CalendarDay(
                    date = date,
                    isSelected = date == selectedDate,
                    isToday = date == today,
                    isCurrentMonth = date.month == currentMonth.month,
                    hasEvents = events.any { it.startDateTime.date == date },
                    onClick = { onDateSelected(date) }
                )
            }
        }
    }
}

@Composable
private fun CalendarDay(
    date: LocalDate,
    isSelected: Boolean,
    isToday: Boolean,
    isCurrentMonth: Boolean,
    hasEvents: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .clip(CircleShape)
            .background(
                when {
                    isSelected -> SelectedDateColor
                    isToday -> TodayColor.copy(alpha = 0.3f)
                    else -> MaterialTheme.colorScheme.surface
                }
            )
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = date.dayOfMonth.toString(),
                style = MaterialTheme.typography.bodyMedium,
                color = when {
                    isSelected -> MaterialTheme.colorScheme.onPrimary
                    isToday -> TodayColor
                    isCurrentMonth -> MaterialTheme.colorScheme.onSurface
                    else -> MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                },
                fontWeight = if (isToday) FontWeight.Bold else FontWeight.Normal
            )
            
            if (hasEvents) {
                Box(
                    modifier = Modifier
                        .size(4.dp)
                        .background(
                            if (isSelected) MaterialTheme.colorScheme.onPrimary
                            else MaterialTheme.colorScheme.primary,
                            CircleShape
                        )
                )
            }
        }
    }
}

private fun generateCalendarDays(startDate: LocalDate, endOfMonth: LocalDate): List<LocalDate> {
    val days = mutableListOf<LocalDate>()
    var currentDate = startDate
    
    // Add days until we've covered the entire month and filled the last week
    while (currentDate <= endOfMonth || days.size % 7 != 0) {
        days.add(currentDate)
        currentDate = currentDate.plus(1, DateTimeUnit.DAY)
    }
    
    return days
}