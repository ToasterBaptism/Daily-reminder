package com.dailyreminder.application.di

import android.content.Context
import androidx.room.Room
import com.dailyreminder.data.local.database.PlannerDatabase
import com.dailyreminder.data.local.database.dao.*
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
            context.applicationContext,
            PlannerDatabase::class.java,
            PlannerDatabase.DATABASE_NAME
        )
            .fallbackToDestructiveMigration()
            .build()
    }
    
    @Provides
    fun provideEventDao(database: PlannerDatabase): EventDao = database.eventDao()
    
    @Provides
    fun provideMealDao(database: PlannerDatabase): MealDao = database.mealDao()
    
    @Provides
    fun provideTaskDao(database: PlannerDatabase): TaskDao = database.taskDao()
    
    @Provides
    fun provideNotificationDao(database: PlannerDatabase): NotificationDao = database.notificationDao()
}