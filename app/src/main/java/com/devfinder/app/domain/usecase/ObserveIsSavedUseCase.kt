package com.devfinder.app.domain.usecase

import com.devfinder.app.domain.repository.SavedRepositoryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveIsSavedUseCase @Inject constructor(
    private val repository: SavedRepositoryRepository
) {
    operator fun invoke(repositoryId: Long): Flow<Boolean> {
        return repository.observeIsSaved(repositoryId)
    }
}
