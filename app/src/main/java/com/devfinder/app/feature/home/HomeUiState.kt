package com.devfinder.app.feature.home

sealed interface HomeUiState {

    data object Loading : HomeUiState

    data class Success(
        val repositories: List<RepositoryUiModel>
    ) : HomeUiState

    data class Error(
        val message: String
    ) : HomeUiState
}
