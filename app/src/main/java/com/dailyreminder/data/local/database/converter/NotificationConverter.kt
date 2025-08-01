package com.dailyreminder.data.local.database.converter

import androidx.room.TypeConverter
import com.dailyreminder.data.model.NotificationAction
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class NotificationConverter {
    
    private val gson = Gson()
    
    // List<NotificationAction>
    @TypeConverter
    fun fromNotificationActionList(actions: List<NotificationAction>): String {
        return gson.toJson(actions)
    }
    
    @TypeConverter
    fun toNotificationActionList(actionsString: String): List<NotificationAction> {
        val type = object : TypeToken<List<NotificationAction>>() {}.type
        return gson.fromJson(actionsString, type) ?: emptyList()
    }
}