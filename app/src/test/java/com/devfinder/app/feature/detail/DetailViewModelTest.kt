package com.devfinder.app.feature.detail

import androidx.lifecycle.SavedStateHandle
import com.devfinder.app.MainDispatcherRule
import com.devfinder.app.domain.model.Repository
import com.devfinder.app.domain.repository.RepositoryRepository
import com.devfinder.app.domain.usecase.GetRepositoryDetailUseCase
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import com.devfinder.app.domain.repository.SavedRepositoryRepository
import com.devfinder.app.domain.usecase.SaveRepositoryUseCase
import com.devfinder.app.domain.usecase.DeleteSavedRepositoryUseCase
import com.devfinder.app.domain.usecase.ObserveIsSavedUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import com.devfinder.app.domain.usecase.GetSavedRepositoryUseCase

class DetailViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `loads repository successfully`() = runBlocking {
        val expected = sampleRepository()

        val viewModel = createViewModel(
            repositoryId = "123",
            repository = FakeRepositoryRepository(
                repository = expected
            )
        )

        val state = viewModel.uiState.value

        assertTrue(state is DetailUiState.Success)

        val success = state as DetailUiState.Success
        assertEquals(expected, success.repository)
    }

    @Test
    fun `shows error when repository request fails`() = runBlocking {
        val viewModel = createViewModel(
            repositoryId = "123",
            repository = FakeRepositoryRepository(
                shouldFail = true
            )
        )

        assertTrue(
            viewModel.uiState.value is DetailUiState.Error
        )
    }

    @Test
    fun `shows error for invalid repository ID`() = runBlocking {
        val viewModel = createViewModel(
            repositoryId = "invalid",
            repository = FakeRepositoryRepository()
        )

        assertTrue(
            viewModel.uiState.value is DetailUiState.Error
        )
    }


    private fun createViewModel(
        repositoryId: String,
        repository: RepositoryRepository,
        savedRepository: SavedRepositoryRepository =
            FakeSavedRepositoryRepository()
    ): DetailViewModel {
        return DetailViewModel(
            getRepositoryDetail = GetRepositoryDetailUseCase(repository),
            getSavedRepository = GetSavedRepositoryUseCase(savedRepository),
            saveRepository = SaveRepositoryUseCase(savedRepository),
            deleteSavedRepository = DeleteSavedRepositoryUseCase(savedRepository),
            observeIsSaved = ObserveIsSavedUseCase(savedRepository),
            savedStateHandle = SavedStateHandle(
                mapOf("repositoryId" to repositoryId)
            )
        )
    }

    private fun sampleRepository() = Repository(
        id = 123L,
        name = "devfinder",
        owner = "developer",
        description = "Android portfolio project",
        language = "Kotlin",
        stars = 150
    )

    private class FakeRepositoryRepository(
        private val repository: Repository? = null,
        private val shouldFail: Boolean = false
    ) : RepositoryRepository {

        override suspend fun getTrendingRepositories(): List<Repository> {
            return emptyList()
        }

        override suspend fun getRepositoryById(
            repositoryId: Long
        ): Repository {
            if (shouldFail) {
                throw IllegalStateException("Simulated network error")
            }

            return requireNotNull(repository)
        }

        override suspend fun searchRepositories(
            query: String
        ): List<Repository> {
            return emptyList()
        }
    }

    private class FakeSavedRepositoryRepository(
        private val savedItem: Repository? = null
    ) : SavedRepositoryRepository {

        override fun observeSavedRepositories(): Flow<List<Repository>> {
            return flowOf(listOfNotNull(savedItem))
        }

        override fun observeIsSaved(repositoryId: Long): Flow<Boolean> {
            return flowOf(savedItem?.id == repositoryId)
        }

        override suspend fun saveRepository(repository: Repository) = Unit

        override suspend fun deleteRepository(repositoryId: Long) = Unit

        override suspend fun getSavedRepositoryById(
            repositoryId: Long
        ): Repository? {
            return savedItem?.takeIf { it.id == repositoryId }
        }
    }

    @Test
    fun `loads saved repository when GitHub request fails`() = runBlocking {
        val expected = sampleRepository()

        val viewModel = createViewModel(
            repositoryId = "123",
            repository = FakeRepositoryRepository(
                shouldFail = true
            ),
            savedRepository = FakeSavedRepositoryRepository(
                savedItem = expected
            )
        )

        val state = viewModel.uiState.value

        assertTrue(state is DetailUiState.Success)

        val success = state as DetailUiState.Success
        assertEquals(expected, success.repository)
    }
}
