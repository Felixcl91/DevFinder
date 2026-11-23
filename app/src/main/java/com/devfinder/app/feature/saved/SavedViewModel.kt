package com.devfinder.app.feature.saved

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devfinder.app.domain.model.Repository
import com.devfinder.app.domain.usecase.ObserveSavedRepositoriesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface SavedUiState {
    data object Loading : SavedUiState
    data class Success(
        val repositories: List<Repository>
    ) : SavedUiState
    data class Error(
        val message: String
    ) : SavedUiState
}

@HiltViewModel
class SavedViewModel @Inject constructor(
    private val observeSavedRepositories: ObserveSavedRepositoriesUseCase
) : ViewModel() {

    private val _uiState =
        MutableStateFlow<SavedUiState>(SavedUiState.Loading)

    val uiState = _uiState.asStateFlow()

    init {
        observeRepositories()
    }

    private fun observeRepositories() {
        viewModelScope.launch {
            try {
                observeSavedRepositories().collect { repositories ->
                    _uiState.value = SavedUiState.Success(repositories)
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _uiState.value = SavedUiState.Error(
                    "Could not load saved repositories."
                )
            }
        }
    }
}
