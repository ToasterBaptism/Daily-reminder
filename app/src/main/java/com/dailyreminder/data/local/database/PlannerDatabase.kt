package com.dailyreminder.data.local.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import android.content.Context
import com.dailyreminder.data.local.database.converter.*
import com.dailyreminder.data.local.database.dao.*
import com.dailyreminder.data.local.database.entity.*

@Database(
    entities = [
        EventEntity::class,
        MealEntity::class,
        TaskEntity::class,
        NotificationEntity::class
    ],
    version = 1,
    exportSchema = true
)
@TypeConverters(
    DateTimeConverter::class,
    EnumConverter::class,
    MealConverter::class,
    TaskConverter::class,
    NotificationConverter::class
)
abstract class PlannerDatabase : RoomDatabase() {
    
    abstract fun eventDao(): EventDao
    abstract fun mealDao(): MealDao
    abstract fun taskDao(): TaskDao
    abstract fun notificationDao(): NotificationDao
    
    companion object {
        const val DATABASE_NAME = "planner_database"
        
        @Volatile
        private var INSTANCE: PlannerDatabase? = null
        
        fun getDatabase(context: Context): PlannerDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PlannerDatabase::class.java,
                    DATABASE_NAME
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}