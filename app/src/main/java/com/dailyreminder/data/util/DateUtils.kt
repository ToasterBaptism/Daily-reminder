package com.dailyreminder.data.util

import kotlinx.datetime.*

object DateUtils {

    fun getCurrentDateTime(): LocalDateTime {
        return Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
    }

    fun getCurrentDate(): LocalDate {
        return Clock.System.todayIn(TimeZone.currentSystemDefault())
    }

    fun formatDate(date: LocalDate): String {
        return "${date.year}-${date.monthNumber.toString().padStart(2, '0')}-${date.dayOfMonth.toString().padStart(2, '0')}"
    }

    fun formatTime(time: LocalTime): String {
        return "${time.hour.toString().padStart(2, '0')}:${time.minute.toString().padStart(2, '0')}"
    }

    fun formatDateTime(dateTime: LocalDateTime): String {
        return "${formatDate(dateTime.date)} ${formatTime(dateTime.time)}"
    }

    fun formatDateTimeForDisplay(dateTime: LocalDateTime): String {
        val now = getCurrentDateTime()
        val today = now.date
        val yesterday = today.minus(1, DateTimeUnit.DAY)
        val tomorrow = today.plus(1, DateTimeUnit.DAY)

        return when (dateTime.date) {
            today -> "Today ${formatTime(dateTime.time)}"
            yesterday -> "Yesterday ${formatTime(dateTime.time)}"
            tomorrow -> "Tomorrow ${formatTime(dateTime.time)}"
            else -> formatDateTime(dateTime)
        }
    }

    fun isToday(date: LocalDate): Boolean {
        return date == getCurrentDate()
    }

    fun isOverdue(dateTime: LocalDateTime): Boolean {
        return dateTime < getCurrentDateTime()
    }

    fun getStartOfDay(date: LocalDate): LocalDateTime {
        return date.atTime(0, 0)
    }

    fun getEndOfDay(date: LocalDate): LocalDateTime {
        return date.atTime(23, 59, 59)
    }

    fun getStartOfWeek(date: LocalDate): LocalDate {
        val dayOfWeek = date.dayOfWeek.ordinal // Monday = 0, Sunday = 6
        return date.minus(dayOfWeek, DateTimeUnit.DAY)
    }

    fun getEndOfWeek(date: LocalDate): LocalDate {
        val dayOfWeek = date.dayOfWeek.ordinal
        return date.plus(6 - dayOfWeek, DateTimeUnit.DAY)
    }

    fun getStartOfMonth(date: LocalDate): LocalDate {
        return LocalDate(date.year, date.month, 1)
    }

    fun getEndOfMonth(date: LocalDate): LocalDate {
        val nextMonth = date.plus(1, DateTimeUnit.MONTH)
        return LocalDate(nextMonth.year, nextMonth.month, 1).minus(1, DateTimeUnit.DAY)
    }

    fun getDaysInMonth(year: Int, month: Month): Int {
        return when (month) {
            Month.FEBRUARY -> if (isLeapYear(year)) 29 else 28
            Month.APRIL, Month.JUNE, Month.SEPTEMBER, Month.NOVEMBER -> 30
            else -> 31
        }
    }

    private fun isLeapYear(year: Int): Boolean {
        return year % 4 == 0 && (year % 100 != 0 || year % 400 == 0)
    }

    fun addMinutes(dateTime: LocalDateTime, minutes: Int): LocalDateTime {
        val instant = dateTime.toInstant(TimeZone.currentSystemDefault())
        val newInstant = instant.plus(minutes, DateTimeUnit.MINUTE)
        return newInstant.toLocalDateTime(TimeZone.currentSystemDefault())
    }

    fun addHours(dateTime: LocalDateTime, hours: Int): LocalDateTime {
        val instant = dateTime.toInstant(TimeZone.currentSystemDefault())
        val newInstant = instant.plus(hours, DateTimeUnit.HOUR)
        return newInstant.toLocalDateTime(TimeZone.currentSystemDefault())
    }

    fun addDays(dateTime: LocalDateTime, days: Int): LocalDateTime {
        val instant = dateTime.toInstant(TimeZone.currentSystemDefault())
        val newInstant = instant.plus(days, DateTimeUnit.DAY, TimeZone.currentSystemDefault())
        return newInstant.toLocalDateTime(TimeZone.currentSystemDefault())
    }

    fun getTimeUntil(targetDateTime: LocalDateTime): kotlin.time.Duration {
        val now = getCurrentDateTime().toInstant(TimeZone.currentSystemDefault())
        val target = targetDateTime.toInstant(TimeZone.currentSystemDefault())
        return target - now
    }

    fun formatDuration(duration: kotlin.time.Duration): String {
        val hours = duration.inWholeHours
        val minutes = duration.inWholeMinutes % 60

        return when {
            hours > 0 -> "${hours}h ${minutes}m"
            minutes > 0 -> "${minutes}m"
            else -> "Now"
        }
    }
}