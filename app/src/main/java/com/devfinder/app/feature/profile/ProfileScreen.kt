package com.devfinder.app.feature.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import android.content.Intent
import android.net.Uri
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Button
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.material3.Surface
import androidx.compose.foundation.shape.RoundedCornerShape

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    fun openUrl(url: String) {
        val intent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse(url)
        )
        context.startActivity(intent)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Text(
            text = "DevFinder",
            style = MaterialTheme.typography.headlineLarge
        )

        Text(
            text = "Discover. Explore. Save.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        HorizontalDivider()

        Text(
            text = "About the app",
            style = MaterialTheme.typography.titleLarge
        )

        Text(
            text = "Explore GitHub repositories, discover " +
                    "projects by technology and save your " +
                    "favorites for later.",
            style = MaterialTheme.typography.bodyLarge
        )

        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Built with",
                    style = MaterialTheme.typography.titleMedium
                )

                val technologies = listOf(
                    "Kotlin",
                    "Jetpack Compose",
                    "MVVM",
                    "Clean Architecture",
                    "Hilt",
                    "Room",
                    "Retrofit",
                    "Coroutines"
                )

                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    technologies.forEach { technology ->
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = MaterialTheme.colorScheme.primaryContainer
                        ) {
                            Text(
                                text = technology,
                                modifier = Modifier.padding(
                                    horizontal = 12.dp,
                                    vertical = 8.dp
                                ),
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    }
                }

            }
        }

        HorizontalDivider()

        Text(
            text = "Developer",
            style = MaterialTheme.typography.titleLarge
        )

        Text(
            text = "Android developer · Portfolio project",
            style = MaterialTheme.typography.bodyLarge
        )

        Button(
            onClick = {
                openUrl(
                    "https://github.com/Felixcl91/DevFinder"
                )
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("View source code")
        }

        OutlinedButton(
            onClick = {
                openUrl(
                    "https://github.com/Felixcl91"
                )
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Developer GitHub")
        }

        Text(
            text = "Version 1.0",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
