package com.devfinder.app.feature.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.devfinder.app.core.designsystem.components.RepositoryCard

@Composable
fun HomeScreen(
    uiState: HomeUiState,
    onRetry: () -> Unit,
    onRepositoryClick: (RepositoryUiModel) -> Unit,
    modifier: Modifier = Modifier
) {
    var searchQuery by rememberSaveable {
        mutableStateOf("")
    }

    var selectedLanguage by rememberSaveable {
        mutableStateOf("All")
    }

    val languages = listOf(
        "All", "Kotlin", "Java", "Python", "JavaScript"
    )

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        when (uiState) {
            HomeUiState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is HomeUiState.Error -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(uiState.message)

                    Spacer(Modifier.height(16.dp))

                    Button(onClick = onRetry) {
                        Text("Retry")
                    }
                }
            }

            is HomeUiState.Success -> {
                val filteredRepositories =
                    uiState.repositories.filter { repository ->
                        val matchesSearch =
                            repository.name.contains(
                                searchQuery,
                                ignoreCase = true
                            ) || repository.owner.contains(
                                searchQuery,
                                ignoreCase = true
                            )

                        val matchesLanguage =
                            selectedLanguage == "All" ||
                                    repository.language == selectedLanguage

                        matchesSearch && matchesLanguage
                    }

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(20.dp),
                    verticalArrangement = Arrangement.spacedBy(18.dp)
                ) {
                    item {
                        Column(
                            verticalArrangement =
                                Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = "Explore repositories",
                                style =
                                    MaterialTheme.typography.headlineMedium
                            )

                            Text(
                                text = "Discover your next favorite project.",
                                color =
                                    MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    item {
                        OutlinedTextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = {
                                Text("Search repositories...")
                            },
                            singleLine = true
                        )
                    }

                    item {
                        LazyRow(
                            horizontalArrangement =
                                Arrangement.spacedBy(8.dp)
                        ) {
                            items(languages) { language ->
                                FilterChip(
                                    selected =
                                        selectedLanguage == language,
                                    onClick = {
                                        selectedLanguage = language
                                    },
                                    label = { Text(language) }
                                )
                            }
                        }
                    }

                    item {
                        Text(
                            text = "Popular repositories",
                            style = MaterialTheme.typography.titleLarge
                        )
                    }

                    if (filteredRepositories.isEmpty()) {
                        item {
                            Text("No repositories found")
                        }
                    }

                    items(
                        items = filteredRepositories,
                        key = { it.id }
                    ) { repository ->
                        RepositoryCard(
                            name = repository.name,
                            owner = repository.owner,
                            description = repository.description,
                            language = repository.language,
                            stars = repository.stars,
                            onClick = {
                                onRepositoryClick(repository)
                            }
                        )
                    }
                }
            }
        }
    }
}

