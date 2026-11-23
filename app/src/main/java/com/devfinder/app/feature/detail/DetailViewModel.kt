package com.devfinder.app.feature.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devfinder.app.domain.usecase.DeleteSavedRepositoryUseCase
import com.devfinder.app.domain.usecase.GetRepositoryDetailUseCase
import com.devfinder.app.domain.usecase.ObserveIsSavedUseCase
import com.devfinder.app.domain.usecase.SaveRepositoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.devfinder.app.domain.usecase.GetSavedRepositoryUseCase

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val getRepositoryDetail: GetRepositoryDetailUseCase,
    private val saveRepository: SaveRepositoryUseCase,
    private val deleteSavedRepository: DeleteSavedRepositoryUseCase,
    private val observeIsSaved: ObserveIsSavedUseCase,
    private val getSavedRepository: GetSavedRepositoryUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val repositoryId: Long? =
        savedStateHandle.get<String>("repositoryId")?.toLongOrNull()

    private val _uiState =
        MutableStateFlow<DetailUiState>(DetailUiState.Loading)

    val uiState = _uiState.asStateFlow()

    private val _isSaved = MutableStateFlow(false)
    val isSaved = _isSaved.asStateFlow()

    private val _isSaving = MutableStateFlow(false)
    val isSaving = _isSaving.asStateFlow()

    private val _saveError = MutableStateFlow<String?>(null)
    val saveError = _saveError.asStateFlow()

    init {
        loadRepository()
        observeSavedState()
    }


    fun loadRepository() {
        val id = repositoryId

        if (id == null) {
            _uiState.value = DetailUiState.Error(
                "Invalid repository ID."
            )
            return
        }

        viewModelScope.launch {
            _uiState.value = DetailUiState.Loading

            try {
                // Primero intentamos obtener datos actualizados de GitHub.
                val repository = getRepositoryDetail(id)

                _uiState.value = DetailUiState.Success(
                    repository = repository
                )
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                // Si GitHub falla, intentamos recuperar el favorito local.
                try {
                    val savedRepository = getSavedRepository(id)

                    if (savedRepository != null) {
                        _uiState.value = DetailUiState.Success(
                            repository = savedRepository
                        )
                    } else {
                        _uiState.value = DetailUiState.Error(
                            "Could not load repository. Please try again."
                        )
                    }
                } catch (localError: CancellationException) {
                    throw localError
                } catch (localError: Exception) {
                    _uiState.value = DetailUiState.Error(
                        "Could not load repository. Please try again."
                    )
                }
            }
        }
    }


    private fun observeSavedState() {
        val id = repositoryId ?: return

        viewModelScope.launch {
            observeIsSaved(id).collect { saved ->
                _isSaved.value = saved
            }
        }
    }

    fun toggleSaved() {
        val repository =
            (_uiState.value as? DetailUiState.Success)?.repository
                ?: return

        if (_isSaving.value) return

        viewModelScope.launch {
            _isSaving.value = true
            _saveError.value = null

            try {
                if (_isSaved.value) {
                    deleteSavedRepository(repository.id)
                } else {
                    saveRepository(repository)
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _saveError.value =
                    "Could not update saved repositories."
            } finally {
                _isSaving.value = false
            }
        }
    }
}

