package com.devfinder.app.domain.usecase

import com.devfinder.app.domain.repository.SavedRepositoryRepository
import javax.inject.Inject

class DeleteSavedRepositoryUseCase @Inject constructor(
    private val repository: SavedRepositoryRepository
) {
    suspend operator fun invoke(repositoryId: Long) {
        repository.deleteRepository(repositoryId)
    }
}
