package com.dailyreminder.data.local.database.converter

import androidx.room.TypeConverter
import com.dailyreminder.data.model.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class MealConverter {
    
    private val gson = Gson()
    
    // Recipe
    @TypeConverter
    fun fromRecipe(recipe: Recipe?): String? {
        return recipe?.let { gson.toJson(it) }
    }
    
    @TypeConverter
    fun toRecipe(recipeString: String?): Recipe? {
        return recipeString?.let { gson.fromJson(it, Recipe::class.java) }
    }
    
    // List<Ingredient>
    @TypeConverter
    fun fromIngredientList(ingredients: List<Ingredient>): String {
        return gson.toJson(ingredients)
    }
    
    @TypeConverter
    fun toIngredientList(ingredientsString: String): List<Ingredient> {
        val type = object : TypeToken<List<Ingredient>>() {}.type
        return gson.fromJson(ingredientsString, type) ?: emptyList()
    }
    
    // NutritionalInfo
    @TypeConverter
    fun fromNutritionalInfo(nutritionalInfo: NutritionalInfo?): String? {
        return nutritionalInfo?.let { gson.toJson(it) }
    }
    
    @TypeConverter
    fun toNutritionalInfo(nutritionalInfoString: String?): NutritionalInfo? {
        return nutritionalInfoString?.let { gson.fromJson(it, NutritionalInfo::class.java) }
    }
    
    // List<DietaryRestriction>
    @TypeConverter
    fun fromDietaryRestrictionList(restrictions: List<DietaryRestriction>): String {
        return gson.toJson(restrictions.map { it.name })
    }
    
    @TypeConverter
    fun toDietaryRestrictionList(restrictionsString: String): List<DietaryRestriction> {
        val type = object : TypeToken<List<String>>() {}.type
        val stringList: List<String> = gson.fromJson(restrictionsString, type) ?: emptyList()
        return stringList.map { DietaryRestriction.valueOf(it) }
    }
}