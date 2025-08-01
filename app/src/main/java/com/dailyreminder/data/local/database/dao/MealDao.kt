package com.dailyreminder.data.local.database.dao

import androidx.room.*
import com.dailyreminder.data.local.database.entity.MealEntity
import com.dailyreminder.data.model.MealType
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDateTime

@Dao
interface MealDao {
    
    @Query("SELECT * FROM meals ORDER BY scheduledDateTime ASC")
    fun getAllMeals(): Flow<List<MealEntity>>
    
    @Query("SELECT * FROM meals WHERE id = :id")
    suspend fun getMealById(id: Long): MealEntity?
    
    @Query("SELECT * FROM meals WHERE DATE(scheduledDateTime) = DATE(:date) ORDER BY scheduledDateTime ASC")
    fun getMealsByDate(date: LocalDateTime): Flow<List<MealEntity>>
    
    @Query("SELECT * FROM meals WHERE scheduledDateTime BETWEEN :startDate AND :endDate ORDER BY scheduledDateTime ASC")
    fun getMealsBetweenDates(startDate: LocalDateTime, endDate: LocalDateTime): Flow<List<MealEntity>>
    
    @Query("SELECT * FROM meals WHERE mealType = :mealType ORDER BY scheduledDateTime ASC")
    fun getMealsByType(mealType: MealType): Flow<List<MealEntity>>
    
    @Query("SELECT * FROM meals WHERE DATE(scheduledDateTime) = DATE(:date) AND mealType = :mealType")
    fun getMealByDateAndType(date: LocalDateTime, mealType: MealType): Flow<MealEntity?>
    
    @Query("SELECT * FROM meals WHERE isCompleted = :isCompleted ORDER BY scheduledDateTime ASC")
    fun getMealsByCompletionStatus(isCompleted: Boolean): Flow<List<MealEntity>>
    
    @Query("SELECT * FROM meals WHERE notificationEnabled = 1 AND scheduledDateTime > :currentTime ORDER BY scheduledDateTime ASC")
    fun getUpcomingMealsWithNotifications(currentTime: LocalDateTime): Flow<List<MealEntity>>
    
    @Query("SELECT * FROM meals WHERE name LIKE '%' || :query || '%' OR notes LIKE '%' || :query || '%' ORDER BY scheduledDateTime ASC")
    fun searchMeals(query: String): Flow<List<MealEntity>>
    
    @Query("SELECT DISTINCT ingredients FROM meals WHERE ingredients LIKE '%' || :ingredient || '%'")
    fun getMealsWithIngredient(ingredient: String): Flow<List<MealEntity>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMeal(meal: MealEntity): Long
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMeals(meals: List<MealEntity>)
    
    @Update
    suspend fun updateMeal(meal: MealEntity)
    
    @Delete
    suspend fun deleteMeal(meal: MealEntity)
    
    @Query("DELETE FROM meals WHERE id = :id")
    suspend fun deleteMealById(id: Long)
    
    @Query("DELETE FROM meals")
    suspend fun deleteAllMeals()
    
    @Query("SELECT COUNT(*) FROM meals")
    suspend fun getMealCount(): Int
    
    @Query("SELECT COUNT(*) FROM meals WHERE isCompleted = 1")
    suspend fun getCompletedMealCount(): Int
    
    @Query("SELECT COUNT(*) FROM meals WHERE DATE(scheduledDateTime) = DATE(:date)")
    suspend fun getMealCountForDate(date: LocalDateTime): Int
}