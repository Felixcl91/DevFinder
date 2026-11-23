package com.devfinder.app.domain.usecase

import com.devfinder.app.domain.model.Repository
import com.devfinder.app.domain.repository.RepositoryRepository
import javax.inject.Inject

class GetTrendingRepositoriesUseCase @Inject constructor(
    private val repository: RepositoryRepository
) {
    suspend operator fun invoke(): List<Repository> {
        return repository.getTrendingRepositories()
    }
}

