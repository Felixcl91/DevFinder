package com.devfinder.app.domain.usecase

import com.devfinder.app.domain.model.Repository
import com.devfinder.app.domain.repository.SavedRepositoryRepository
import javax.inject.Inject

class GetSavedRepositoryUseCase @Inject constructor(
    private val repository: SavedRepositoryRepository
) {
    suspend operator fun invoke(repositoryId: Long): Repository? {
        return repository.getSavedRepositoryById(repositoryId)
    }
}
