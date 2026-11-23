package com.devfinder.app.core.di

import android.content.Context
import androidx.room.Room
import com.devfinder.app.data.local.DevFinderDatabase
import com.devfinder.app.data.local.dao.SavedRepositoryDao
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
    fun provideDatabase(
        @ApplicationContext context: Context
    ): DevFinderDatabase {
        return Room.databaseBuilder(
            context,
            DevFinderDatabase::class.java,
            "devfinder_database"
        ).build()
    }

    @Provides
    fun provideSavedRepositoryDao(
        database: DevFinderDatabase
    ): SavedRepositoryDao {
        return database.savedRepositoryDao()
    }
}
