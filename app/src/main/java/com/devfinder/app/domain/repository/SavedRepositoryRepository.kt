package com.devfinder.app.domain.repository

import com.devfinder.app.domain.model.Repository
import kotlinx.coroutines.flow.Flow

interface SavedRepositoryRepository {

    fun observeSavedRepositories(): Flow<List<Repository>>

    fun observeIsSaved(repositoryId: Long): Flow<Boolean>

    suspend fun saveRepository(repository: Repository)

    suspend fun deleteRepository(repositoryId: Long)

    suspend fun getSavedRepositoryById(
        repositoryId: Long
    ): Repository?

}
