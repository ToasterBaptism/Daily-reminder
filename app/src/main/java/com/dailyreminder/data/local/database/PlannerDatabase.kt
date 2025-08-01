package com.dailyreminder.data.local.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import android.content.Context
import com.dailyreminder.data.local.database.converter.DateTimeConverter
import com.dailyreminder.data.local.database.dao.EventDao
import com.dailyreminder.data.local.database.dao.MealDao
import com.dailyreminder.data.local.database.dao.NotificationDao
import com.dailyreminder.data.local.database.dao.TaskDao
import com.dailyreminder.data.local.database.entity.EventEntity
import com.dailyreminder.data.local.database.entity.MealEntity
import com.dailyreminder.data.local.database.entity.NotificationEntity
import com.dailyreminder.data.local.database.entity.TaskEntity

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
@TypeConverters(DateTimeConverter::class)
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