package com.devfinder.app.domain.usecase

import com.devfinder.app.domain.model.Repository
import com.devfinder.app.domain.repository.SavedRepositoryRepository
import javax.inject.Inject

class SaveRepositoryUseCase @Inject constructor(
    private val repository: SavedRepositoryRepository
) {
    suspend operator fun invoke(item: Repository) {
        repository.saveRepository(item)
    }
}
