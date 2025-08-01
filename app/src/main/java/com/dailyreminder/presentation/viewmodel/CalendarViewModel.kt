package com.dailyreminder.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dailyreminder.data.local.repository.PlannerRepository
import com.dailyreminder.data.model.Event
import com.dailyreminder.data.util.DateUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import javax.inject.Inject

@HiltViewModel
class CalendarViewModel @Inject constructor(
    private val repository: PlannerRepository
) : ViewModel() {

    private val _selectedDate = MutableStateFlow(DateUtils.getCurrentDate())
    val selectedDate: StateFlow<LocalDate> = _selectedDate.asStateFlow()

    private val _events = MutableStateFlow<List<Event>>(emptyList())
    val events: StateFlow<List<Event>> = _events.asStateFlow()

    val uiState: StateFlow<CalendarUiState> = combine(
        selectedDate,
        events
    ) { selectedDate, events ->
        CalendarUiState(
            selectedDate = selectedDate,
            events = events,
            selectedDateEvents = events.filter { event ->
                event.startDateTime.date == selectedDate
            }
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = CalendarUiState()
    )

    init {
        loadEvents()
    }

    fun selectDate(date: LocalDate) {
        _selectedDate.value = date
    }

    private fun loadEvents() {
        viewModelScope.launch {
            repository.getAllEvents().collect { eventList ->
                _events.value = eventList
            }
        }
    }

    fun refreshEvents() {
        loadEvents()
    }
}

data class CalendarUiState(
    val selectedDate: LocalDate = DateUtils.getCurrentDate(),
    val events: List<Event> = emptyList(),
    val selectedDateEvents: List<Event> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)