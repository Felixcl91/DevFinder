package com.devfinder.app.domain.usecase

import com.devfinder.app.domain.model.Repository
import com.devfinder.app.domain.repository.RepositoryRepository
import javax.inject.Inject

class SearchRepositoriesUseCase @Inject constructor(
    private val repository: RepositoryRepository
) {
    suspend operator fun invoke(query: String): List<Repository> {
        if (query.isBlank()) {
            return emptyList()
        }

        return repository.searchRepositories(query.trim())
    }
}
