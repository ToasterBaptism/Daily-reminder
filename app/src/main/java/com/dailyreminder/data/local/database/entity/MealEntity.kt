package com.dailyreminder.data.local.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.dailyreminder.data.local.database.converter.DateTimeConverter
import com.dailyreminder.data.local.database.converter.EnumConverter
import com.dailyreminder.data.local.database.converter.MealConverter
import com.dailyreminder.data.model.*
import kotlinx.datetime.LocalDateTime

@Entity(tableName = "meals")
@TypeConverters(DateTimeConverter::class, EnumConverter::class, MealConverter::class)
data class MealEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val mealType: MealType,
    val scheduledDateTime: LocalDateTime,
    val recipe: Recipe?,
    val ingredients: List<Ingredient>,
    val preparationTimeMinutes: Int,
    val cookingTimeMinutes: Int,
    val servings: Int,
    val nutritionalInfo: NutritionalInfo?,
    val dietaryRestrictions: List<DietaryRestriction>,
    val notes: String,
    val isCompleted: Boolean,
    val notificationEnabled: Boolean,
    val notificationMinutesBefore: Int,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)