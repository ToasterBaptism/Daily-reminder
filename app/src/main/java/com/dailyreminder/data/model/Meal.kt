package com.dailyreminder.data.model

import kotlinx.datetime.LocalDateTime

data class Meal(
    val id: Long = 0,
    val name: String,
    val description: String = "",
    val mealType: MealType,
    val scheduledDateTime: LocalDateTime,
    val preparationTimeMinutes: Int = 30,
    val servings: Int = 1,
    val ingredients: List<String> = emptyList(),
    val instructions: List<String> = emptyList(),
    val nutritionalInfo: NutritionalInfo = NutritionalInfo(),
    val dietaryTags: List<DietaryTag> = emptyList(),
    val reminderMinutes: Int = 30,
    val isCompleted: Boolean = false,
    val createdAt: LocalDateTime = LocalDateTime.parse("2024-01-01T00:00:00"),
    val updatedAt: LocalDateTime = LocalDateTime.parse("2024-01-01T00:00:00")
)

data class NutritionalInfo(
    val calories: Int = 0,
    val protein: Float = 0f,
    val carbohydrates: Float = 0f,
    val fat: Float = 0f,
    val fiber: Float = 0f,
    val sugar: Float = 0f,
    val sodium: Float = 0f
)

enum class MealType {
    BREAKFAST,
    LUNCH,
    DINNER,
    SNACK,
    DESSERT,
    DRINK
}

enum class DietaryTag {
    VEGETARIAN,
    VEGAN,
    GLUTEN_FREE,
    DAIRY_FREE,
    KETO,
    LOW_CARB,
    HIGH_PROTEIN,
    LOW_SODIUM,
    ORGANIC,
    PALEO
}