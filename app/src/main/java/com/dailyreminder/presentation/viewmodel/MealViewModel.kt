package com.dailyreminder.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dailyreminder.data.local.repository.PlannerRepository
import com.dailyreminder.data.model.*
import com.dailyreminder.domain.usecase.meal.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class MealViewModel @Inject constructor(
    private val repository: PlannerRepository,
    private val getMealsUseCase: GetMealsUseCase
) : ViewModel() {
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()
    
    private val _selectedDate = MutableStateFlow(kotlinx.datetime.Clock.System.todayIn(kotlinx.datetime.TimeZone.currentSystemDefault()))
    val selectedDate: StateFlow<LocalDate> = _selectedDate.asStateFlow()
    
    private val _filterMealType = MutableStateFlow<MealType?>(null)
    val filterMealType: StateFlow<MealType?> = _filterMealType.asStateFlow()
    
    private val _showCompleted = MutableStateFlow(false)
    val showCompleted: StateFlow<Boolean> = _showCompleted.asStateFlow()
    
    val allMeals = getMealsUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
    
    val mealsForSelectedDate = combine(
        allMeals,
        selectedDate,
        filterMealType,
        showCompleted
    ) { meals, date, mealType, completed ->
        meals
            .filter { meal ->
                meal.scheduledDateTime.date == date &&
                (mealType == null || meal.mealType == mealType) &&
                (completed || !meal.isCompleted)
            }
            .sortedBy { it.scheduledDateTime.time }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )
    
    val mealPlanStats = allMeals.map { meals ->
        val today = kotlinx.datetime.Clock.System.todayIn(kotlinx.datetime.TimeZone.currentSystemDefault())
        val todayMeals = meals.filter { it.scheduledDateTime.date == today }
        
        MealPlanStats(
            totalMealsToday = todayMeals.size,
            completedMealsToday = todayMeals.count { it.isCompleted },
            upcomingMeals = meals.count { meal ->
                !meal.isCompleted && 
                meal.scheduledDateTime > kotlinx.datetime.Clock.System.now().toLocalDateTime(kotlinx.datetime.TimeZone.currentSystemDefault())
            },
            totalRecipes = meals.mapNotNull { it.recipe }.distinctBy { it.id }.size
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = MealPlanStats()
    )
    
    fun selectDate(date: LocalDate) {
        _selectedDate.value = date
    }
    
    fun setFilterMealType(mealType: MealType?) {
        _filterMealType.value = mealType
    }
    
    fun setShowCompleted(show: Boolean) {
        _showCompleted.value = show
    }
    
    fun toggleMealCompletion(mealId: Long) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val meal = allMeals.value.find { it.id == mealId }
                if (meal != null) {
                    val updatedMeal = meal.copy(
                        isCompleted = !meal.isCompleted,
                        completedDateTime = if (!meal.isCompleted) 
                            kotlinx.datetime.Clock.System.now().toLocalDateTime(kotlinx.datetime.TimeZone.currentSystemDefault()) 
                        else null,
                        updatedDateTime = kotlinx.datetime.Clock.System.now().toLocalDateTime(kotlinx.datetime.TimeZone.currentSystemDefault())
                    )
                    repository.updateMeal(updatedMeal)
                }
                _error.value = null
            } catch (e: Exception) {
                _error.value = e.message ?: "Failed to update meal"
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun createMeal(
        name: String,
        mealType: MealType,
        scheduledDateTime: LocalDateTime,
        recipe: Recipe? = null,
        servings: Int = 1,
        preparationTimeMinutes: Int = 0,
        cookingTimeMinutes: Int = 0,
        dietaryRestrictions: List<DietaryRestriction> = emptyList(),
        notificationEnabled: Boolean = true
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val meal = Meal(
                    id = 0, // Will be auto-generated
                    name = name,
                    mealType = mealType,
                    scheduledDateTime = scheduledDateTime,
                    recipe = recipe,
                    servings = servings,
                    preparationTimeMinutes = preparationTimeMinutes,
                    cookingTimeMinutes = cookingTimeMinutes,
                    isCompleted = false,
                    completedDateTime = null,
                    dietaryRestrictions = dietaryRestrictions,
                    notificationEnabled = notificationEnabled,
                    createdDateTime = kotlinx.datetime.Clock.System.now().toLocalDateTime(kotlinx.datetime.TimeZone.currentSystemDefault()),
                    updatedDateTime = kotlinx.datetime.Clock.System.now().toLocalDateTime(kotlinx.datetime.TimeZone.currentSystemDefault())
                )
                repository.insertMeal(meal)
                _error.value = null
            } catch (e: Exception) {
                _error.value = e.message ?: "Failed to create meal"
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun clearError() {
        _error.value = null
    }
}

data class MealPlanStats(
    val totalMealsToday: Int = 0,
    val completedMealsToday: Int = 0,
    val upcomingMeals: Int = 0,
    val totalRecipes: Int = 0
) {
    val completionRateToday: Float = if (totalMealsToday > 0) completedMealsToday.toFloat() / totalMealsToday else 0f
}