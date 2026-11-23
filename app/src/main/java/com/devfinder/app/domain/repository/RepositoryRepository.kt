package com.devfinder.app.domain.repository

import com.devfinder.app.domain.model.Repository

interface RepositoryRepository {

    suspend fun getTrendingRepositories(): List<Repository>

    suspend fun getRepositoryById(
        repositoryId: Long
    ): Repository

    suspend fun searchRepositories(
        query: String
    ): List<Repository>

}
