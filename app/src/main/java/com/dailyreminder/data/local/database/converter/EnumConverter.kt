package com.dailyreminder.data.local.database.converter

import androidx.room.TypeConverter
import com.dailyreminder.data.model.*

class EnumConverter {
    
    // EventCategory
    @TypeConverter
    fun fromEventCategory(category: EventCategory): String = category.name
    
    @TypeConverter
    fun toEventCategory(categoryString: String): EventCategory = 
        EventCategory.valueOf(categoryString)
    
    // Priority
    @TypeConverter
    fun fromPriority(priority: Priority): String = priority.name
    
    @TypeConverter
    fun toPriority(priorityString: String): Priority = 
        Priority.valueOf(priorityString)
    
    // Recurrence
    @TypeConverter
    fun fromRecurrence(recurrence: Recurrence): String = recurrence.name
    
    @TypeConverter
    fun toRecurrence(recurrenceString: String): Recurrence = 
        Recurrence.valueOf(recurrenceString)
    
    // MealType
    @TypeConverter
    fun fromMealType(mealType: MealType): String = mealType.name
    
    @TypeConverter
    fun toMealType(mealTypeString: String): MealType = 
        MealType.valueOf(mealTypeString)
    
    // TaskCategory
    @TypeConverter
    fun fromTaskCategory(category: TaskCategory): String = category.name
    
    @TypeConverter
    fun toTaskCategory(categoryString: String): TaskCategory = 
        TaskCategory.valueOf(categoryString)
    
    // NotificationType
    @TypeConverter
    fun fromNotificationType(type: NotificationType): String = type.name
    
    @TypeConverter
    fun toNotificationType(typeString: String): NotificationType = 
        NotificationType.valueOf(typeString)
    
    // NotificationPriority
    @TypeConverter
    fun fromNotificationPriority(priority: NotificationPriority): String = priority.name
    
    @TypeConverter
    fun toNotificationPriority(priorityString: String): NotificationPriority = 
        NotificationPriority.valueOf(priorityString)
    
    // RecipeDifficulty
    @TypeConverter
    fun fromRecipeDifficulty(difficulty: RecipeDifficulty): String = difficulty.name
    
    @TypeConverter
    fun toRecipeDifficulty(difficultyString: String): RecipeDifficulty = 
        RecipeDifficulty.valueOf(difficultyString)
    
    // NotificationActionType
    @TypeConverter
    fun fromNotificationActionType(actionType: NotificationActionType): String = actionType.name
    
    @TypeConverter
    fun toNotificationActionType(actionTypeString: String): NotificationActionType = 
        NotificationActionType.valueOf(actionTypeString)
}