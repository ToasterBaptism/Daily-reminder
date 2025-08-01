package com.dailyreminder.data.local.database.dao

import androidx.room.*
import com.dailyreminder.data.local.database.entity.MealEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MealDao {

    @Query("SELECT * FROM meals ORDER BY scheduledDateTime ASC")
    fun getAllMeals(): Flow<List<MealEntity>>

    @Query("SELECT * FROM meals WHERE id = :id")
    suspend fun getMealById(id: Long): MealEntity?

    @Query("SELECT * FROM meals WHERE scheduledDateTime BETWEEN :startDate AND :endDate ORDER BY scheduledDateTime ASC")
    fun getMealsBetweenDates(startDate: String, endDate: String): Flow<List<MealEntity>>

    @Query("SELECT * FROM meals WHERE mealType = :mealType ORDER BY scheduledDateTime ASC")
    fun getMealsByType(mealType: String): Flow<List<MealEntity>>

    @Query("SELECT * FROM meals WHERE isCompleted = :isCompleted ORDER BY scheduledDateTime ASC")
    fun getMealsByCompletionStatus(isCompleted: Boolean): Flow<List<MealEntity>>

    @Query("SELECT * FROM meals WHERE name LIKE '%' || :searchQuery || '%' OR description LIKE '%' || :searchQuery || '%' OR ingredients LIKE '%' || :searchQuery || '%' ORDER BY scheduledDateTime ASC")
    fun searchMeals(searchQuery: String): Flow<List<MealEntity>>

    @Query("SELECT * FROM meals WHERE ingredients LIKE '%' || :ingredient || '%' ORDER BY scheduledDateTime ASC")
    fun getMealsWithIngredient(ingredient: String): Flow<List<MealEntity>>

    @Query("SELECT * FROM meals WHERE dietaryTags LIKE '%' || :tag || '%' ORDER BY scheduledDateTime ASC")
    fun getMealsByDietaryTag(tag: String): Flow<List<MealEntity>>

    @Query("SELECT DISTINCT ingredients FROM meals WHERE ingredients != ''")
    suspend fun getAllIngredients(): List<String>

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

    @Query("SELECT COUNT(*) FROM meals WHERE isCompleted = 0 AND scheduledDateTime < :currentDateTime")
    suspend fun getOverdueMealCount(currentDateTime: String): Int
}