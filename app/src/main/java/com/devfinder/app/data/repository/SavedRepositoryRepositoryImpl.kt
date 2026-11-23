package com.devfinder.app.data.repository

import com.devfinder.app.data.local.dao.SavedRepositoryDao
import com.devfinder.app.data.mapper.toDomain
import com.devfinder.app.data.mapper.toEntity
import com.devfinder.app.domain.model.Repository
import com.devfinder.app.domain.repository.SavedRepositoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SavedRepositoryRepositoryImpl @Inject constructor(
    private val dao: SavedRepositoryDao
) : SavedRepositoryRepository {

    override fun observeSavedRepositories(): Flow<List<Repository>> {
        return dao.observeSavedRepositories().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun observeIsSaved(repositoryId: Long): Flow<Boolean> {
        return dao.observeIsSaved(repositoryId)
    }

    override suspend fun saveRepository(repository: Repository) {
        dao.saveRepository(repository.toEntity())
    }

    override suspend fun deleteRepository(repositoryId: Long) {
        dao.deleteRepository(repositoryId)
    }

    override suspend fun getSavedRepositoryById(
        repositoryId: Long
    ): Repository? {
        return dao.getSavedRepositoryById(repositoryId)?.toDomain()
    }
}
