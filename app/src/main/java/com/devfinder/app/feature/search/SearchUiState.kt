package com.devfinder.app.feature.search

import com.devfinder.app.domain.model.Repository

sealed interface SearchUiState {

    data object Initial : SearchUiState

    data object Loading : SearchUiState

    data class Success(
        val repositories: List<Repository>
    ) : SearchUiState

    data class Error(
        val message: String
    ) : SearchUiState
}
