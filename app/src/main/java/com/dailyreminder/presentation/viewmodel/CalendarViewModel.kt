package com.dailyreminder.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dailyreminder.data.model.*
import com.dailyreminder.data.util.DateUtils
import com.dailyreminder.domain.usecase.event.GetEventsUseCase
import com.dailyreminder.domain.usecase.meal.GetMealsUseCase
import com.dailyreminder.domain.usecase.task.GetTasksUseCase
import com.dailyreminder.domain.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class CalendarViewModel @Inject constructor(
    private val getEventsUseCase: GetEventsUseCase,
    private val getMealsUseCase: GetMealsUseCase,
    private val getTasksUseCase: GetTasksUseCase
) : ViewModel() {
    
    private val _selectedDate = MutableStateFlow(DateUtils.today())
    val selectedDate: StateFlow<LocalDate> = _selectedDate.asStateFlow()
    
    private val _calendarViewType = MutableStateFlow(CalendarViewType.MONTH)
    val calendarViewType: StateFlow<CalendarViewType> = _calendarViewType.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()
    
    // Events for selected date
    val eventsForSelectedDate: StateFlow<List<Event>> = selectedDate
        .flatMapLatest { date ->
            val startOfDay = DateUtils.startOfDay(date)
            getEventsUseCase.getEventsByDate(startOfDay)
                .map { result ->
                    when (result) {
                        is Result.Success -> result.data
                        is Result.Error -> {
                            _error.value = result.exception.message
                            emptyList()
                        }
                        is Result.Loading -> {
                            _isLoading.value = true
                            emptyList()
                        }
                    }
                }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
    
    // Meals for selected date
    val mealsForSelectedDate: StateFlow<List<Meal>> = selectedDate
        .flatMapLatest { date ->
            val startOfDay = DateUtils.startOfDay(date)
            getMealsUseCase.getMealsByDate(startOfDay)
                .map { result ->
                    when (result) {
                        is Result.Success -> result.data
                        is Result.Error -> {
                            _error.value = result.exception.message
                            emptyList()
                        }
                        is Result.Loading -> {
                            _isLoading.value = true
                            emptyList()
                        }
                    }
                }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
    
    // Tasks for selected date
    val tasksForSelectedDate: StateFlow<List<Task>> = selectedDate
        .flatMapLatest { date ->
            val startOfDay = DateUtils.startOfDay(date)
            getTasksUseCase.getTasksByDate(startOfDay)
                .map { result ->
                    when (result) {
                        is Result.Success -> result.data
                        is Result.Error -> {
                            _error.value = result.exception.message
                            emptyList()
                        }
                        is Result.Loading -> {
                            _isLoading.value = true
                            emptyList()
                        }
                    }
                }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
    
    // Combined items for selected date
    val itemsForSelectedDate: StateFlow<CalendarDayItems> = combine(
        eventsForSelectedDate,
        mealsForSelectedDate,
        tasksForSelectedDate
    ) { events, meals, tasks ->
        CalendarDayItems(
            events = events,
            meals = meals,
            tasks = tasks
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = CalendarDayItems()
    )
    
    // Events for current month (for calendar overview)
    val eventsForCurrentMonth: StateFlow<List<Event>> = selectedDate
        .flatMapLatest { date ->
            val startOfMonth = DateUtils.startOfMonth(date)
            val endOfMonth = DateUtils.endOfMonth(date)
            getEventsUseCase.getEventsBetweenDates(
                DateUtils.startOfDay(startOfMonth),
                DateUtils.endOfDay(endOfMonth)
            ).map { result ->
                when (result) {
                    is Result.Success -> result.data
                    is Result.Error -> {
                        _error.value = result.exception.message
                        emptyList()
                    }
                    is Result.Loading -> emptyList()
                }
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
    
    fun selectDate(date: LocalDate) {
        _selectedDate.value = date
        clearError()
    }
    
    fun selectToday() {
        _selectedDate.value = DateUtils.today()
        clearError()
    }
    
    fun navigateToNextDay() {
        _selectedDate.value = _selectedDate.value.plus(1, kotlinx.datetime.DateTimeUnit.DAY)
    }
    
    fun navigateToPreviousDay() {
        _selectedDate.value = _selectedDate.value.minus(1, kotlinx.datetime.DateTimeUnit.DAY)
    }
    
    fun navigateToNextMonth() {
        _selectedDate.value = _selectedDate.value.plus(1, kotlinx.datetime.DateTimeUnit.MONTH)
    }
    
    fun navigateToPreviousMonth() {
        _selectedDate.value = _selectedDate.value.minus(1, kotlinx.datetime.DateTimeUnit.MONTH)
    }
    
    fun setCalendarViewType(viewType: CalendarViewType) {
        _calendarViewType.value = viewType
    }
    
    fun clearError() {
        _error.value = null
    }
    
    fun refreshData() {
        viewModelScope.launch {
            _isLoading.value = true
            // Data will be refreshed automatically through flows
            _isLoading.value = false
        }
    }
    
    data class CalendarDayItems(
        val events: List<Event> = emptyList(),
        val meals: List<Meal> = emptyList(),
        val tasks: List<Task> = emptyList()
    ) {
        val totalItems: Int get() = events.size + meals.size + tasks.size
        val hasItems: Boolean get() = totalItems > 0
    }
    
    enum class CalendarViewType {
        MONTH, WEEK, DAY
    }
}