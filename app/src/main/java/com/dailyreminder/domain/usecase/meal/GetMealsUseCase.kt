package com.dailyreminder.domain.usecase.meal

import com.dailyreminder.data.local.repository.PlannerRepository
import com.dailyreminder.data.model.Meal
import com.dailyreminder.data.model.MealType
import com.dailyreminder.domain.util.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.datetime.LocalDateTime
import javax.inject.Inject

class GetMealsUseCase @Inject constructor(
    private val repository: PlannerRepository
) {
    
    fun getAllMeals(): Flow<Result<List<Meal>>> {
        return repository.getAllMeals()
            .map { Result.Success(it) as Result<List<Meal>> }
            .catch { emit(Result.Error(it)) }
    }
    
    fun getMealsByDate(date: LocalDateTime): Flow<Result<List<Meal>>> {
        return repository.getMealsByDate(date)
            .map { Result.Success(it) as Result<List<Meal>> }
            .catch { emit(Result.Error(it)) }
    }
    
    fun getMealsByType(mealType: MealType): Flow<Result<List<Meal>>> {
        return repository.getMealsByType(mealType)
            .map { Result.Success(it) as Result<List<Meal>> }
            .catch { emit(Result.Error(it)) }
    }
    
    fun getMealByDateAndType(date: LocalDateTime, mealType: MealType): Flow<Result<Meal?>> {
        return repository.getMealByDateAndType(date, mealType)
            .map { Result.Success(it) as Result<Meal?> }
            .catch { emit(Result.Error(it)) }
    }
    
    fun getUpcomingMealsWithNotifications(currentTime: LocalDateTime): Flow<Result<List<Meal>>> {
        return repository.getUpcomingMealsWithNotifications(currentTime)
            .map { Result.Success(it) as Result<List<Meal>> }
            .catch { emit(Result.Error(it)) }
    }
    
    fun searchMeals(query: String): Flow<Result<List<Meal>>> {
        return repository.searchMeals(query)
            .map { Result.Success(it) as Result<List<Meal>> }
            .catch { emit(Result.Error(it)) }
    }
    
    suspend fun getMealById(id: Long): Result<Meal?> {
        return try {
            val meal = repository.getMealById(id)
            Result.Success(meal)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}