package com.devfinder.app.feature.home

import com.devfinder.app.MainDispatcherRule
import com.devfinder.app.domain.model.Repository
import com.devfinder.app.domain.repository.RepositoryRepository
import com.devfinder.app.domain.usecase.GetTrendingRepositoriesUseCase
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class HomeViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `loadRepositories returns success when repository succeeds`() = runBlocking {

        val fakeRepository = FakeRepositoryRepository(
            repositories = listOf(
                Repository(
                    id = 1L,
                    name = "devfinder",
                    owner = "developer",
                    description = "Portfolio project",
                    language = "Kotlin",
                    stars = 150
                )
            )
        )

        val viewModel = HomeViewModel(
            GetTrendingRepositoriesUseCase(fakeRepository)
        )

        val state = viewModel.uiState.value

        assertTrue(state is HomeUiState.Success)

        val success = state as HomeUiState.Success

        assertEquals(1, success.repositories.size)
        assertEquals("devfinder", success.repositories.first().name)
        assertEquals("Kotlin", success.repositories.first().language)
    }

    @Test
    fun `loadRepositories returns error when repository fails`() = runBlocking {

        val fakeRepository = FakeRepositoryRepository(
            shouldFail = true
        )

        val viewModel = HomeViewModel(
            GetTrendingRepositoriesUseCase(fakeRepository)
        )

        val state = viewModel.uiState.value

        assertTrue(state is HomeUiState.Error)
    }

    private class FakeRepositoryRepository(
        private val repositories: List<Repository> = emptyList(),
        private val shouldFail: Boolean = false
    ) : RepositoryRepository {

        override suspend fun getTrendingRepositories(): List<Repository> {
            if (shouldFail) {
                throw IllegalStateException("Simulated network error")
            }

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
