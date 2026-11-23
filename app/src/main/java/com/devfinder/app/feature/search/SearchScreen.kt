package com.devfinder.app.feature.search

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.devfinder.app.core.designsystem.components.RepositoryCard
import com.devfinder.app.domain.model.Repository
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.layout.Row

@Composable
fun SearchScreen(
    query: String,
    uiState: SearchUiState,
    selectedLanguage: String,
    selectedStars: String,
    onQueryChange: (String) -> Unit,
    onLanguageSelected: (String) -> Unit,
    onStarsSelected: (String) -> Unit,
    onSearch: () -> Unit,
    onRepositoryClick: (Repository) -> Unit,
    modifier: Modifier = Modifier
)
 {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Search repositories",
            style = MaterialTheme.typography.headlineMedium
        )

        OutlinedTextField(
            value = query,
            onValueChange = onQueryChange,
            modifier = Modifier.fillMaxWidth(),
            label = {
                Text("Repository or technology")
            },
            placeholder = {
                Text("e.g. android compose")
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(
                onSearch = {
                    onSearch()
                }
            )
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            listOf(
                "All",
                "Kotlin",
                "Java",
                "Python",
                "JavaScript"
            ).forEach { language ->

                FilterChip(
                    selected = selectedLanguage == language,
                    onClick = {
                        onLanguageSelected(language)
                    },
                    label = {
                        Text(language)
                    }
                )
            }
        }

        Text(
            text = "Minimum stars",
            style = MaterialTheme.typography.titleSmall
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            listOf(
                "Any stars",
                "100+",
                "1,000+",
                "10,000+"
            ).forEach { stars ->

                FilterChip(
                    selected = selectedStars == stars,
                    onClick = {
                        onStarsSelected(stars)
                    },
                    label = {
                        Text(stars)
                    }
                )
            }
        }

        when (uiState) {
            SearchUiState.Initial -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Discover repositories on GitHub",
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            SearchUiState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is SearchUiState.Error -> {
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = uiState.message,
                        color = MaterialTheme.colorScheme.error
                    )

                    OutlinedButton(onClick = onSearch) {
                        Text("Retry")
                    }
                }
            }

            is SearchUiState.Success -> {
                if (uiState.repositories.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No repositories found.",
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                } else {
                    Text(
                        text = "${uiState.repositories.size} repositories found",
                        style = MaterialTheme.typography.titleMedium
                    )

                    LazyColumn(
                        modifier = Modifier.weight(1f),
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
