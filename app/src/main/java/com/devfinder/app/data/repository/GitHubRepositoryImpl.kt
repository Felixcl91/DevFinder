package com.devfinder.app.data.repository

import com.devfinder.app.data.mapper.toDomain
import com.devfinder.app.data.remote.GitHubApiService
import com.devfinder.app.domain.model.Repository
import com.devfinder.app.domain.repository.RepositoryRepository

class GitHubRepositoryImpl(
    private val apiService: GitHubApiService
) : RepositoryRepository {

    override suspend fun getTrendingRepositories(): List<Repository> {
        val response = apiService.searchRepositories(
            query = "stars:>1000"
        )

        return response.items.map { it.toDomain() }
    }

    override suspend fun getRepositoryById(
        repositoryId: Long): Repository {
        return apiService
            .getRepositoryById(repositoryId)
            .toDomain()
    }

    override suspend fun searchRepositories(
        query: String
    ): List<Repository> {
        return apiService.searchRepositories(
            query = query
        ).items.map { it.toDomain() }
    }
}
