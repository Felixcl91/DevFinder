package com.devfinder.app.feature.detail

import com.devfinder.app.domain.model.Repository

sealed interface DetailUiState {

    data object Loading : DetailUiState

    data class Success(
        val repository: Repository
    ) : DetailUiState

    data class Error(
        val message: String
    ) : DetailUiState
}
