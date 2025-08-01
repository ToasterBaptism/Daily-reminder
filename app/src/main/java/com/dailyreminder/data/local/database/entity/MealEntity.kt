package com.dailyreminder.data.local.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.dailyreminder.data.model.DietaryTag
import com.dailyreminder.data.model.Meal
import com.dailyreminder.data.model.MealType
import com.dailyreminder.data.model.NutritionalInfo
import kotlinx.datetime.LocalDateTime

@Entity(tableName = "meals")
data class MealEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val description: String,
    val mealType: String,
    val scheduledDateTime: String, // ISO string format
    val preparationTimeMinutes: Int,
    val servings: Int,
    val ingredients: String, // JSON string
    val instructions: String, // JSON string
    val calories: Int,
    val protein: Float,
    val carbohydrates: Float,
    val fat: Float,
    val fiber: Float,
    val sugar: Float,
    val sodium: Float,
    val dietaryTags: String, // JSON string
    val reminderMinutes: Int,
    val isCompleted: Boolean,
    val createdAt: String, // ISO string format
    val updatedAt: String // ISO string format
)

fun MealEntity.toMeal(): Meal {
    return Meal(
        id = id,
        name = name,
        description = description,
        mealType = MealType.valueOf(mealType),
        scheduledDateTime = LocalDateTime.parse(scheduledDateTime),
        preparationTimeMinutes = preparationTimeMinutes,
        servings = servings,
        ingredients = parseJsonStringList(ingredients),
        instructions = parseJsonStringList(instructions),
        nutritionalInfo = NutritionalInfo(
            calories = calories,
            protein = protein,
            carbohydrates = carbohydrates,
            fat = fat,
            fiber = fiber,
            sugar = sugar,
            sodium = sodium
        ),
        dietaryTags = parseJsonStringList(dietaryTags).map { DietaryTag.valueOf(it) },
        reminderMinutes = reminderMinutes,
        isCompleted = isCompleted,
        createdAt = LocalDateTime.parse(createdAt),
        updatedAt = LocalDateTime.parse(updatedAt)
    )
}

fun Meal.toEntity(): MealEntity {
    return MealEntity(
        id = id,
        name = name,
        description = description,
        mealType = mealType.name,
        scheduledDateTime = scheduledDateTime.toString(),
        preparationTimeMinutes = preparationTimeMinutes,
        servings = servings,
        ingredients = ingredients.toJsonString(),
        instructions = instructions.toJsonString(),
        calories = nutritionalInfo.calories,
        protein = nutritionalInfo.protein,
        carbohydrates = nutritionalInfo.carbohydrates,
        fat = nutritionalInfo.fat,
        fiber = nutritionalInfo.fiber,
        sugar = nutritionalInfo.sugar,
        sodium = nutritionalInfo.sodium,
        dietaryTags = dietaryTags.map { it.name }.toJsonString(),
        reminderMinutes = reminderMinutes,
        isCompleted = isCompleted,
        createdAt = createdAt.toString(),
        updatedAt = updatedAt.toString()
    )
}

private fun parseJsonStringList(json: String): List<String> {
    return if (json.isBlank()) emptyList() else json.split(",").map { it.trim() }
}

private fun List<String>.toJsonString(): String {
    return this.joinToString(",")
}