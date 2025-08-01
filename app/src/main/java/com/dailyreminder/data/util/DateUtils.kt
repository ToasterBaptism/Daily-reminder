package com.dailyreminder.data.util

import kotlinx.datetime.*
import kotlinx.datetime.format.DateTimeFormat
import kotlinx.datetime.format.FormatStringsInDatetimeFormats
import kotlinx.datetime.format.byUnicodePattern

object DateUtils {
    
    private val timeZone = TimeZone.currentSystemDefault()
    
    @OptIn(FormatStringsInDatetimeFormats::class)
    private val dateFormat = LocalDate.Format {
        byUnicodePattern("yyyy-MM-dd")
    }
    
    @OptIn(FormatStringsInDatetimeFormats::class)
    private val timeFormat = LocalTime.Format {
        byUnicodePattern("HH:mm")
    }
    
    @OptIn(FormatStringsInDatetimeFormats::class)
    private val dateTimeFormat = LocalDateTime.Format {
        byUnicodePattern("yyyy-MM-dd HH:mm")
    }
    
    fun now(): LocalDateTime = Clock.System.now().toLocalDateTime(timeZone)
    
    fun today(): LocalDate = Clock.System.todayIn(timeZone)
    
    fun startOfDay(date: LocalDate): LocalDateTime = 
        date.atTime(0, 0)
    
    fun endOfDay(date: LocalDate): LocalDateTime = 
        date.atTime(23, 59, 59)
    
    fun startOfWeek(date: LocalDate): LocalDate {
        val dayOfWeek = date.dayOfWeek.ordinal // Monday = 0, Sunday = 6
        return date.minus(dayOfWeek, DateTimeUnit.DAY)
    }
    
    fun endOfWeek(date: LocalDate): LocalDate {
        val dayOfWeek = date.dayOfWeek.ordinal
        return date.plus(6 - dayOfWeek, DateTimeUnit.DAY)
    }
    
    fun startOfMonth(date: LocalDate): LocalDate = 
        LocalDate(date.year, date.month, 1)
    
    fun endOfMonth(date: LocalDate): LocalDate {
        val nextMonth = date.plus(1, DateTimeUnit.MONTH)
        return LocalDate(nextMonth.year, nextMonth.month, 1).minus(1, DateTimeUnit.DAY)
    }
    
    fun isToday(date: LocalDate): Boolean = date == today()
    
    fun isThisWeek(date: LocalDate): Boolean {
        val today = today()
        val startOfWeek = startOfWeek(today)
        val endOfWeek = endOfWeek(today)
        return date >= startOfWeek && date <= endOfWeek
    }
    
    fun isThisMonth(date: LocalDate): Boolean {
        val today = today()
        return date.year == today.year && date.month == today.month
    }
    
    fun isPast(dateTime: LocalDateTime): Boolean = dateTime < now()
    
    fun isFuture(dateTime: LocalDateTime): Boolean = dateTime > now()
    
    fun isOverdue(dateTime: LocalDateTime?): Boolean = 
        dateTime?.let { it < now() } ?: false
    
    fun daysBetween(start: LocalDate, end: LocalDate): Int = 
        start.daysUntil(end)
    
    fun minutesBetween(start: LocalDateTime, end: LocalDateTime): Long {
        val startInstant = start.toInstant(timeZone)
        val endInstant = end.toInstant(timeZone)
        return (endInstant - startInstant).inWholeMinutes
    }
    
    fun addMinutes(dateTime: LocalDateTime, minutes: Int): LocalDateTime = 
        dateTime.plus(minutes, DateTimeUnit.MINUTE)
    
    fun addHours(dateTime: LocalDateTime, hours: Int): LocalDateTime = 
        dateTime.plus(hours, DateTimeUnit.HOUR)
    
    fun addDays(dateTime: LocalDateTime, days: Int): LocalDateTime = 
        dateTime.plus(days, DateTimeUnit.DAY)
    
    fun addWeeks(dateTime: LocalDateTime, weeks: Int): LocalDateTime = 
        dateTime.plus(weeks * 7, DateTimeUnit.DAY)
    
    fun addMonths(dateTime: LocalDateTime, months: Int): LocalDateTime = 
        dateTime.plus(months, DateTimeUnit.MONTH)
    
    fun addYears(dateTime: LocalDateTime, years: Int): LocalDateTime = 
        dateTime.plus(years, DateTimeUnit.YEAR)
    
    fun formatDate(date: LocalDate): String = dateFormat.format(date)
    
    fun formatTime(time: LocalTime): String = timeFormat.format(time)
    
    fun formatDateTime(dateTime: LocalDateTime): String = dateTimeFormat.format(dateTime)
    
    fun formatRelativeTime(dateTime: LocalDateTime): String {
        val now = now()
        val diff = minutesBetween(now, dateTime)
        
        return when {
            diff < 0 -> {
                val absDiff = -diff
                when {
                    absDiff < 60 -> "${absDiff}m ago"
                    absDiff < 1440 -> "${absDiff / 60}h ago"
                    absDiff < 10080 -> "${absDiff / 1440}d ago"
                    else -> formatDate(dateTime.date)
                }
            }
            diff == 0L -> "Now"
            diff < 60 -> "In ${diff}m"
            diff < 1440 -> "In ${diff / 60}h"
            diff < 10080 -> "In ${diff / 1440}d"
            else -> formatDate(dateTime.date)
        }
    }
    
    fun getWeekDays(startDate: LocalDate): List<LocalDate> {
        return (0..6).map { startDate.plus(it, DateTimeUnit.DAY) }
    }
    
    fun getMonthDays(year: Int, month: Month): List<LocalDate> {
        val firstDay = LocalDate(year, month, 1)
        val lastDay = endOfMonth(firstDay)
        val days = mutableListOf<LocalDate>()
        
        var currentDay = firstDay
        while (currentDay <= lastDay) {
            days.add(currentDay)
            currentDay = currentDay.plus(1, DateTimeUnit.DAY)
        }
        
        return days
    }
    
    fun isWeekend(date: LocalDate): Boolean = 
        date.dayOfWeek == DayOfWeek.SATURDAY || date.dayOfWeek == DayOfWeek.SUNDAY
    
    fun getNextRecurrence(dateTime: LocalDateTime, recurrence: com.dailyreminder.data.model.Recurrence): LocalDateTime? {
        return when (recurrence) {
            com.dailyreminder.data.model.Recurrence.NONE -> null
            com.dailyreminder.data.model.Recurrence.DAILY -> addDays(dateTime, 1)
            com.dailyreminder.data.model.Recurrence.WEEKLY -> addWeeks(dateTime, 1)
            com.dailyreminder.data.model.Recurrence.MONTHLY -> addMonths(dateTime, 1)
            com.dailyreminder.data.model.Recurrence.YEARLY -> addYears(dateTime, 1)
        }
    }
    
    fun getTimeUntilNotification(scheduledTime: LocalDateTime, minutesBefore: Int): LocalDateTime = 
        scheduledTime.minus(minutesBefore, DateTimeUnit.MINUTE)
    
    fun parseDate(dateString: String): LocalDate? = 
        try { LocalDate.parse(dateString) } catch (e: Exception) { null }
    
    fun parseDateTime(dateTimeString: String): LocalDateTime? = 
        try { LocalDateTime.parse(dateTimeString) } catch (e: Exception) { null }
}