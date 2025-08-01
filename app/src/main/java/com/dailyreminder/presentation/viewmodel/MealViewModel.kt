package com.dailyreminder.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dailyreminder.data.local.repository.PlannerRepository
import com.dailyreminder.data.model.Meal
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MealViewModel @Inject constructor(
    private val repository: PlannerRepository
) : ViewModel() {

    private val _meals = MutableStateFlow<List<Meal>>(emptyList())
    val meals: StateFlow<List<Meal>> = _meals.asStateFlow()

    val uiState: StateFlow<MealUiState> = meals.map { meals ->
        MealUiState(
            meals = meals.sortedBy { it.scheduledDateTime }
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = MealUiState()
    )

    init {
        loadMeals()
    }

    private fun loadMeals() {
        viewModelScope.launch {
            repository.getAllMeals().collect { mealList ->
                _meals.value = mealList
            }
        }
    }

    fun refreshMeals() {
        loadMeals()
    }
}

data class MealUiState(
    val meals: List<Meal> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)