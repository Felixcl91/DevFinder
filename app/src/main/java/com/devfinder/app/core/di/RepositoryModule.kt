package com.devfinder.app.core.di

import com.devfinder.app.data.remote.GitHubApiService
import com.devfinder.app.data.repository.GitHubRepositoryImpl
import com.devfinder.app.domain.repository.RepositoryRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import com.devfinder.app.data.local.dao.SavedRepositoryDao
import com.devfinder.app.data.repository.SavedRepositoryRepositoryImpl
import com.devfinder.app.domain.repository.SavedRepositoryRepository

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideRepository(
        apiService: GitHubApiService
    ): RepositoryRepository {
        return GitHubRepositoryImpl(apiService)
    }


    @Provides
    @Singleton
    fun provideSavedRepositoryRepository(
        dao: SavedRepositoryDao
    ): SavedRepositoryRepository {
        return SavedRepositoryRepositoryImpl(dao)
    }

}
