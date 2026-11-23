package com.devfinder.app.domain.usecase

import com.devfinder.app.domain.model.Repository
import com.devfinder.app.domain.repository.RepositoryRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

class GetTrendingRepositoriesUseCaseTest {

    @Test
    fun `invoke returns repositories from repository`() = runBlocking {

        val expectedRepositories = listOf(
            Repository(
                id = 1L,
                name = "devfinder",
                owner = "developer",
                description = "Android portfolio project",
                language = "Kotlin",
                stars = 150
            )
        )

        val fakeRepository = FakeRepositoryRepository(
            expectedRepositories
        )

        val useCase = GetTrendingRepositoriesUseCase(
            repository = fakeRepository
        )

        val result = useCase()

        assertEquals(expectedRepositories, result)
        assertEquals(1, fakeRepository.calls)
    }

    private class FakeRepositoryRepository(
        private val repositories: List<Repository>
    ) : RepositoryRepository {

        var calls = 0
            private set

        override suspend fun getTrendingRepositories(): List<Repository> {
            calls++
            return repositories
        }

        override suspend fun getRepositoryById(
            repositoryId: Long): Repository {
            return repositories.first { it.id == repositoryId }
        }

        override suspend fun searchRepositories(
            query: String
        ): List<Repository> {
            return emptyList()
        }
    }
}
