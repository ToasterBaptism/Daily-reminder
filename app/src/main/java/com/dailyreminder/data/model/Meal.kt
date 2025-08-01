package com.dailyreminder.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.datetime.LocalDateTime

@Parcelize
data class Meal(
    val id: Long = 0,
    val name: String,
    val mealType: MealType,
    val scheduledDateTime: LocalDateTime,
    val recipe: Recipe? = null,
    val ingredients: List<Ingredient> = emptyList(),
    val preparationTimeMinutes: Int = 0,
    val cookingTimeMinutes: Int = 0,
    val servings: Int = 1,
    val nutritionalInfo: NutritionalInfo? = null,
    val dietaryRestrictions: List<DietaryRestriction> = emptyList(),
    val notes: String = "",
    val isCompleted: Boolean = false,
    val notificationEnabled: Boolean = true,
    val notificationMinutesBefore: Int = 30,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
) : Parcelable

@Parcelize
data class Recipe(
    val name: String,
    val instructions: List<String>,
    val difficulty: RecipeDifficulty = RecipeDifficulty.EASY,
    val cuisine: String = "",
    val tags: List<String> = emptyList()
) : Parcelable

@Parcelize
data class Ingredient(
    val name: String,
    val quantity: String,
    val unit: String = "",
    val isOptional: Boolean = false
) : Parcelable

@Parcelize
data class NutritionalInfo(
    val calories: Int = 0,
    val protein: Double = 0.0,
    val carbohydrates: Double = 0.0,
    val fat: Double = 0.0,
    val fiber: Double = 0.0,
    val sugar: Double = 0.0,
    val sodium: Double = 0.0
) : Parcelable

enum class MealType {
    BREAKFAST,
    LUNCH,
    DINNER,
    SNACK
}

enum class RecipeDifficulty {
    EASY,
    MEDIUM,
    HARD
}

enum class DietaryRestriction {
    VEGETARIAN,
    VEGAN,
    GLUTEN_FREE,
    DAIRY_FREE,
    KETO,
    PALEO,
    LOW_CARB,
    LOW_FAT,
    HALAL,
    KOSHER
}