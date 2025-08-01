package com.dailyreminder.application.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.dailyreminder.data.local.repository.PlannerRepository
import com.dailyreminder.data.util.BackupManager
import com.dailyreminder.data.util.EncryptionUtils
import com.dailyreminder.domain.util.ResourceProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
        return context.dataStore
    }

    @Provides
    @Singleton
    fun provideResourceProvider(@ApplicationContext context: Context): ResourceProvider {
        return ResourceProvider(context)
    }

    @Provides
    @Singleton
    fun provideEncryptionUtils(): EncryptionUtils {
        return EncryptionUtils()
    }

    @Provides
    @Singleton
    fun provideBackupManager(
        @ApplicationContext context: Context,
        repository: PlannerRepository,
        encryptionUtils: EncryptionUtils
    ): BackupManager {
        return BackupManager(context, repository, encryptionUtils)
    }
}