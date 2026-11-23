package com.devfinder.app.feature.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devfinder.app.domain.usecase.SearchRepositoriesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchRepositories: SearchRepositoriesUseCase
) : ViewModel() {

    private val _query = MutableStateFlow("")

    val languages = listOf(
        "All",
        "Kotlin",
        "Java",
        "Python",
        "JavaScript"
    )

    private val _selectedLanguage = MutableStateFlow("All")
    val selectedLanguage = _selectedLanguage.asStateFlow()

    val starFilters = listOf(
        "Any stars",
        "100+",
        "1,000+",
        "10,000+"
    )

    private val _selectedStars = MutableStateFlow("Any stars")
    val selectedStars = _selectedStars.asStateFlow()

    val query = _query.asStateFlow()

    private val _uiState =
        MutableStateFlow<SearchUiState>(SearchUiState.Initial)

    val uiState = _uiState.asStateFlow()

    private var searchJob: Job? = null

    fun onQueryChange(newQuery: String) {
        _query.value = newQuery

        searchJob?.cancel()

        if (newQuery.isBlank()) {
            _uiState.value = SearchUiState.Initial
            return
        }

        _uiState.value = SearchUiState.Loading

        searchJob = viewModelScope.launch {
            delay(600)
            performSearch(newQuery.trim())
        }
    }

    fun search() {
        searchJob?.cancel()

        val currentQuery = _query.value.trim()

        if (currentQuery.isBlank()) {
            _uiState.value = SearchUiState.Initial
            return
        }

        searchJob = viewModelScope.launch {
            performSearch(currentQuery)
        }
    }

    private suspend fun performSearch(query: String) {
        _uiState.value = SearchUiState.Loading

        try {

            val language = _selectedLanguage.value
            val stars = _selectedStars.value

            val languageFilter = if (language == "All") {
                ""
            } else {
                " language:${language.lowercase()}"
            }

            val starsFilter = when (stars) {
                "100+" -> " stars:>=100"
                "1,000+" -> " stars:>=1000"
                "10,000+" -> " stars:>=10000"
                else -> ""
            }

            val githubQuery = "$query$languageFilter$starsFilter"

            val repositories = searchRepositories(githubQuery)

            _uiState.value = SearchUiState.Success(
                repositories = repositories
            )
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            _uiState.value = SearchUiState.Error(
                "Could not search repositories. Please try again."
            )
        }
    }

    fun onLanguageSelected(language: String) {
        if (language !in languages) return
        if (_selectedLanguage.value == language) return

        _selectedLanguage.value = language

        // Reutilizamos el debounce existente.
        onQueryChange(_query.value)
    }

    fun onStarsSelected(stars: String) {
        if (stars !in starFilters) return
        if (_selectedStars.value == stars) return

        _selectedStars.value = stars

        // Reutilizamos el debounce existente.
        onQueryChange(_query.value)
    }
}

