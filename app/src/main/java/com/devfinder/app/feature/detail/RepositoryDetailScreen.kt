package com.devfinder.app.feature.detail

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp


@Composable
fun RepositoryDetailScreen(
    uiState: DetailUiState,
    isSaved: Boolean,
    isSaving: Boolean,
    saveError: String?,
    onBack: () -> Unit,
    onRetry: () -> Unit,
    onOpenGitHub: (owner: String, name: String) -> Unit,
    onToggleSaved: () -> Unit,
    modifier: Modifier = Modifier
)
 {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        TextButton(onClick = onBack) {
            Text("← Back")
        }

        Text(
            text = "Repository details",
            style = MaterialTheme.typography.headlineMedium
        )

        when (uiState) {
            DetailUiState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(240.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is DetailUiState.Error -> {
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = uiState.message,
                        color = MaterialTheme.colorScheme.error
                    )

                    Button(onClick = onRetry) {
                        Text("Retry")
                    }
                }
            }

            is DetailUiState.Success -> {
                val repository = uiState.repository

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Text(
                            text = repository.name,
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = "by ${repository.owner}",
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        HorizontalDivider()

                        Text(
                            text = repository.description.ifBlank {
                                "No description available."
                            }
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = repository.language,
                                color = MaterialTheme.colorScheme.primary
                            )

                            Text("★ ${repository.stars}")
                        }
                    }
                }

                Button(
                    onClick = {
                        onOpenGitHub(
                            repository.owner,
                            repository.name
                        )
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("View on GitHub")
                }

                OutlinedButton(
                    onClick = onToggleSaved,
                    enabled = !isSaving,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        when {
                            isSaving -> "Updating..."
                            isSaved -> "★ Remove from saved"
                            else -> "☆ Save repository"
                        }
                    )
                }

                if (saveError != null) {
                    Text(
                        text = saveError,
                        color = MaterialTheme.colorScheme.error
                    )
                }

            }
        }
    }
}

