package com.dailyreminder.application.di

import android.content.Context
import androidx.room.Room
import com.dailyreminder.data.local.database.PlannerDatabase
import com.dailyreminder.data.local.database.dao.EventDao
import com.dailyreminder.data.local.database.dao.MealDao
import com.dailyreminder.data.local.database.dao.NotificationDao
import com.dailyreminder.data.local.database.dao.TaskDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun providePlannerDatabase(@ApplicationContext context: Context): PlannerDatabase {
        return Room.databaseBuilder(
            context,
            PlannerDatabase::class.java,
            "planner_database"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideEventDao(database: PlannerDatabase): EventDao {
        return database.eventDao()
    }

    @Provides
    fun provideMealDao(database: PlannerDatabase): MealDao {
        return database.mealDao()
    }

    @Provides
    fun provideTaskDao(database: PlannerDatabase): TaskDao {
        return database.taskDao()
    }

    @Provides
    fun provideNotificationDao(database: PlannerDatabase): NotificationDao {
        return database.notificationDao()
    }
}