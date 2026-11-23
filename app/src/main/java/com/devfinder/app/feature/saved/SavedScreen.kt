package com.devfinder.app.feature.saved

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.devfinder.app.core.designsystem.components.RepositoryCard
import com.devfinder.app.domain.model.Repository

@Composable
fun SavedScreen(
    uiState: SavedUiState,
    onRepositoryClick: (Repository) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Saved repositories",
            style = MaterialTheme.typography.headlineMedium
        )

        when (uiState) {
            SavedUiState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is SavedUiState.Error -> {
                Text(
                    text = uiState.message,
                    color = MaterialTheme.colorScheme.error
                )
            }

            is SavedUiState.Success -> {
                if (uiState.repositories.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No saved repositories yet.\nExplore and save your favorites!",
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        contentPadding = PaddingValues(bottom = 16.dp)
                    ) {
                        items(
                            items = uiState.repositories,
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
}
