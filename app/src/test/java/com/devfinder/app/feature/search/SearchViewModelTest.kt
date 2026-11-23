package com.devfinder.app.feature.search

import com.devfinder.app.MainDispatcherRule
import com.devfinder.app.domain.model.Repository
import com.devfinder.app.domain.repository.RepositoryRepository
import com.devfinder.app.domain.usecase.SearchRepositoriesUseCase
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest

@OptIn(ExperimentalCoroutinesApi::class)
class SearchViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule(
        StandardTestDispatcher()
    )

    @Test
    fun `initial state is Initial`() {
        val viewModel = createViewModel()

        assertEquals(
            SearchUiState.Initial,
            viewModel.uiState.value
        )
    }

    @Test
    fun `search returns repositories successfully`() = runTest(mainDispatcherRule.testDispatcher) {
        val expected = sampleRepository()

        val viewModel = createViewModel(
            repositories = listOf(expected)
        )

        viewModel.onQueryChange("kotlin")

        // Ejecutamos la búsqueda inmediatamente.
        viewModel.search()

        advanceUntilIdle()

        val state = viewModel.uiState.value

        assertTrue(state is SearchUiState.Success)
        assertEquals(
            listOf(expected),
            (state as SearchUiState.Success).repositories
        )
    }

    @Test
    fun `search failure shows error`() = runTest(mainDispatcherRule.testDispatcher) {
        val viewModel = createViewModel(
            shouldFail = true
        )

        viewModel.onQueryChange("android")
        viewModel.search()

        advanceUntilIdle()

        assertTrue(
            viewModel.uiState.value is SearchUiState.Error
        )
    }

    @Test
    fun `empty query does not search`() {
        val viewModel = createViewModel()

        viewModel.onQueryChange("   ")
        viewModel.search()

        assertEquals(
            SearchUiState.Initial,
            viewModel.uiState.value
        )
    }


    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `search waits 600ms before calling repository`() =
        runTest(mainDispatcherRule.testDispatcher) {

            val fakeRepository = CountingRepositoryRepository()

            val viewModel = SearchViewModel(
                searchRepositories = SearchRepositoriesUseCase(
                    fakeRepository
                )
            )

            viewModel.onQueryChange("kotlin")

            // Ejecutamos las tareas pendientes en el instante inicial.
            runCurrent()

            assertEquals(0, fakeRepository.searchCount)

            // Todavía no han transcurrido los 600 ms.
            advanceTimeBy(599)
            runCurrent()

            assertEquals(0, fakeRepository.searchCount)

            // Alcanzamos exactamente los 600 ms.
            advanceTimeBy(1)
            runCurrent()

            assertEquals(1, fakeRepository.searchCount)

            assertTrue(
                viewModel.uiState.value is SearchUiState.Success
            )
        }


    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `selecting Kotlin adds language filter to search`() =
        runTest(mainDispatcherRule.testDispatcher) {

            val fakeRepository = CountingRepositoryRepository()

            val viewModel = SearchViewModel(
                searchRepositories = SearchRepositoriesUseCase(
                    fakeRepository
                )
            )

            viewModel.onQueryChange("android")
            viewModel.onLanguageSelected("Kotlin")

            advanceUntilIdle()

            assertEquals(1, fakeRepository.searchCount)

            assertEquals(
                "android language:kotlin",
                fakeRepository.lastQuery
            )

            assertEquals(
                "Kotlin",
                viewModel.selectedLanguage.value
            )
        }


    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `selecting All removes language filter`() =
        runTest(mainDispatcherRule.testDispatcher) {

            val fakeRepository = CountingRepositoryRepository()

            val viewModel = SearchViewModel(
                searchRepositories = SearchRepositoriesUseCase(
                    fakeRepository
                )
            )

            viewModel.onQueryChange("android")
            viewModel.onLanguageSelected("Kotlin")

            advanceUntilIdle()

            assertEquals(
                "android language:kotlin",
                fakeRepository.lastQuery
            )

            viewModel.onLanguageSelected("All")

            advanceUntilIdle()

            assertEquals(
                "android",
                fakeRepository.lastQuery
            )

            assertEquals(
                "All",
                viewModel.selectedLanguage.value
            )

            assertEquals(2, fakeRepository.searchCount)
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `search combines language and stars filters`() =
        runTest(mainDispatcherRule.testDispatcher) {

            val fakeRepository = CountingRepositoryRepository()

            val viewModel = SearchViewModel(
                searchRepositories = SearchRepositoriesUseCase(
                    fakeRepository
                )
            )

            viewModel.onQueryChange("android")
            viewModel.onLanguageSelected("Kotlin")
            viewModel.onStarsSelected("1,000+")

            advanceUntilIdle()

            assertEquals(
                "android language:kotlin stars:>=1000",
                fakeRepository.lastQuery
            )

            assertEquals(1, fakeRepository.searchCount)

            assertEquals(
                "Kotlin",
                viewModel.selectedLanguage.value
            )

            assertEquals(
                "1,000+",
                viewModel.selectedStars.value
            )
        }

    private fun createViewModel(
        repositories: List<Repository> = emptyList(),
        shouldFail: Boolean = false
    ): SearchViewModel {
        val repository = FakeRepositoryRepository(
            repositories = repositories,
            shouldFail = shouldFail
        )

        return SearchViewModel(
            searchRepositories = SearchRepositoriesUseCase(repository)
        )
    }

    private fun sampleRepository() = Repository(
        id = 123L,
        name = "DevFinder",
        owner = "developer",
        description = "Android project",
        language = "Kotlin",
        stars = 150
    )

    private class FakeRepositoryRepository(
        private val repositories: List<Repository>,
        private val shouldFail: Boolean
    ) : RepositoryRepository {

        override suspend fun getTrendingRepositories(): List<Repository> {
            return emptyList()
        }

        override suspend fun getRepositoryById(
            repositoryId: Long
        ): Repository {
            return repositories.first { it.id == repositoryId }
        }

        override suspend fun searchRepositories(
            query: String
        ): List<Repository> {
            if (shouldFail) {
                throw IllegalStateException("Simulated network error")
            }

            return repositories
        }
    }


    private class CountingRepositoryRepository : RepositoryRepository {

        var lastQuery: String? = null
            private set

        var searchCount = 0
            private set

        override suspend fun getTrendingRepositories(): List<Repository> {
            return emptyList()
        }

        override suspend fun getRepositoryById(
            repositoryId: Long
        ): Repository {
            error("Not needed for this test")
        }

        override suspend fun searchRepositories(
            query: String
        ): List<Repository> {
            searchCount++
            lastQuery = query
            return emptyList()
        }
    }

}
