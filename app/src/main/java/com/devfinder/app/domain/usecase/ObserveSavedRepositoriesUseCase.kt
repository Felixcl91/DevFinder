package com.devfinder.app.domain.usecase

import com.devfinder.app.domain.model.Repository
import com.devfinder.app.domain.repository.SavedRepositoryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveSavedRepositoriesUseCase @Inject constructor(
    private val repository: SavedRepositoryRepository
) {
    operator fun invoke(): Flow<List<Repository>> {
        return repository.observeSavedRepositories()
    }
}
