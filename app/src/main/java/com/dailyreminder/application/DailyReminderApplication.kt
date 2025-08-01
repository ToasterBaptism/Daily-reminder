package com.dailyreminder.application

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class DailyReminderApplication : Application() {
    
    override fun onCreate() {
        super.onCreate()
        
        // Initialize any app-wide components here
        // For example: crash reporting, analytics (if we had them), etc.
        // Since this is an offline-only app, we keep initialization minimal
    }
}