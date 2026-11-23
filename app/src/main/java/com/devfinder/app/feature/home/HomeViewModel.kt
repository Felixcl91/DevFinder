package com.devfinder.app.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devfinder.app.domain.usecase.GetTrendingRepositoriesUseCase
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getTrendingRepositories: GetTrendingRepositoriesUseCase
) : ViewModel() {

    private val _uiState =
        MutableStateFlow<HomeUiState>(HomeUiState.Loading)

    val uiState = _uiState.asStateFlow()

    init {
        loadRepositories()
    }

    fun loadRepositories() {
        viewModelScope.launch {
            _uiState.value = HomeUiState.Loading

            try {
                val repositories = getTrendingRepositories()

                _uiState.value = HomeUiState.Success(
                    repositories = repositories.map { repository ->
                        RepositoryUiModel(
                            id = repository.id,
                            name = repository.name,
                            owner = repository.owner,
                            description = repository.description,
                            language = repository.language,
                            stars = repository.stars
                        )
                    }
                )
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _uiState.value = HomeUiState.Error(
                    message = "Could not load repositories. Check your connection."
                )
            }
        }
    }
}
