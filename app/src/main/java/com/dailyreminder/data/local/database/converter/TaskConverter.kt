package com.dailyreminder.data.local.database.converter

import androidx.room.TypeConverter
import com.dailyreminder.data.model.Subtask
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class TaskConverter {
    
    private val gson = Gson()
    
    // List<Subtask>
    @TypeConverter
    fun fromSubtaskList(subtasks: List<Subtask>): String {
        return gson.toJson(subtasks)
    }
    
    @TypeConverter
    fun toSubtaskList(subtasksString: String): List<Subtask> {
        val type = object : TypeToken<List<Subtask>>() {}.type
        return gson.fromJson(subtasksString, type) ?: emptyList()
    }
    
    // List<String> for tags
    @TypeConverter
    fun fromStringList(strings: List<String>): String {
        return gson.toJson(strings)
    }
    
    @TypeConverter
    fun toStringList(stringsString: String): List<String> {
        val type = object : TypeToken<List<String>>() {}.type
        return gson.fromJson(stringsString, type) ?: emptyList()
    }
}