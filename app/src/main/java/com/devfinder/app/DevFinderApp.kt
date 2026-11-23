package com.devfinder.app

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.devfinder.app.core.navigation.DevFinderDestination
import com.devfinder.app.core.navigation.DevFinderRoute
import com.devfinder.app.core.navigation.PlaceholderScreen
import com.devfinder.app.feature.detail.RepositoryDetailScreen
import com.devfinder.app.feature.home.HomeScreen
import com.devfinder.app.feature.home.HomeUiState
import com.devfinder.app.feature.home.HomeViewModel
import com.devfinder.app.feature.home.RepositoryUiModel
import com.devfinder.app.feature.detail.DetailViewModel
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.devfinder.app.feature.saved.SavedScreen
import com.devfinder.app.feature.saved.SavedViewModel
import com.devfinder.app.feature.search.SearchScreen
import com.devfinder.app.feature.search.SearchViewModel
import com.devfinder.app.feature.profile.ProfileScreen

@Composable
fun DevFinderApp() {

    val navController = rememberNavController()
    val uriHandler = LocalUriHandler.current

    val homeViewModel: HomeViewModel = hiltViewModel()

    val homeUiState by homeViewModel.uiState
        .collectAsStateWithLifecycle()

    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    val bottomRoutes = setOf(
        DevFinderRoute.HOME,
        DevFinderRoute.SEARCH,
        DevFinderRoute.SAVED,
        DevFinderRoute.PROFILE
    )

    Scaffold(
        bottomBar = {
            if (currentRoute in bottomRoutes) {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.surface
                ) {
                    DevFinderDestination.entries.forEach { destination ->

                        val route = when (destination) {
                            DevFinderDestination.HOME -> DevFinderRoute.HOME
                            DevFinderDestination.SEARCH -> DevFinderRoute.SEARCH
                            DevFinderDestination.SAVED -> DevFinderRoute.SAVED
                            DevFinderDestination.PROFILE -> DevFinderRoute.PROFILE
                        }

                        NavigationBarItem(
                            selected = currentRoute == route,
                            onClick = {
                                navController.navigate(route) {
                                    popUpTo(
                                        navController.graph.findStartDestination().id
                                    ) {
                                        saveState = true
                                    }

                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = { Text(destination.symbol) },
                            label = { Text(destination.title) },
                            alwaysShowLabel = true
                        )
                    }
                }
            }
        }
    ) { innerPadding ->

        NavHost(
            navController = navController,
            startDestination = DevFinderRoute.HOME,
            modifier = Modifier.padding(innerPadding)
        ) {

            composable(DevFinderRoute.HOME) {
                HomeScreen(
                    uiState = homeUiState,
                    onRetry = homeViewModel::loadRepositories,
                    onRepositoryClick = { repository ->
                        navController.navigate(
                            DevFinderRoute.detail(repository.id)
                        )
                    }
                )
            }

            composable(DevFinderRoute.SEARCH) {

                val searchViewModel: SearchViewModel = hiltViewModel()

                val query by searchViewModel.query
                    .collectAsStateWithLifecycle()

                val searchUiState by searchViewModel.uiState
                    .collectAsStateWithLifecycle()

                val selectedLanguage by searchViewModel.selectedLanguage
                    .collectAsStateWithLifecycle()

                val selectedStars by searchViewModel.selectedStars
                    .collectAsStateWithLifecycle()

                SearchScreen(
                    query = query,
                    uiState = searchUiState,
                    onQueryChange = searchViewModel::onQueryChange,
                    onSearch = searchViewModel::search,
                    selectedLanguage = selectedLanguage,
                    onLanguageSelected = searchViewModel::onLanguageSelected,
                    selectedStars = selectedStars,
                    onStarsSelected = searchViewModel::onStarsSelected,
                    onRepositoryClick = { repository ->
                        navController.navigate(
                            DevFinderRoute.detail(repository.id)
                        )
                    }
                )
            }

            composable(DevFinderRoute.SAVED) {

                val savedViewModel: SavedViewModel = hiltViewModel()

                val savedUiState by savedViewModel.uiState
                    .collectAsStateWithLifecycle()

                SavedScreen(
                    uiState = savedUiState,
                    onRepositoryClick = { repository ->
                        navController.navigate(
                            DevFinderRoute.detail(repository.id)
                        )
                    }
                )
            }

            composable(DevFinderRoute.PROFILE) {
                ProfileScreen()
            }

            composable(DevFinderRoute.DETAIL) { entry ->

                val detailViewModel: DetailViewModel = hiltViewModel(entry)

                val detailUiState by detailViewModel.uiState
                    .collectAsStateWithLifecycle()

                val isSaved by detailViewModel.isSaved
                    .collectAsStateWithLifecycle()

                val isSaving by detailViewModel.isSaving
                    .collectAsStateWithLifecycle()

                val saveError by detailViewModel.saveError
                    .collectAsStateWithLifecycle()

                RepositoryDetailScreen(
                    uiState = detailUiState,
                    isSaved = isSaved,
                    isSaving = isSaving,
                    saveError = saveError,
                    onBack = {
                        navController.popBackStack()
                    },
                    onRetry = detailViewModel::loadRepository,
                    onOpenGitHub = { owner, name ->
                        uriHandler.openUri(
                            "https://github.com/$owner/$name"
                        )
                    },
                    onToggleSaved = detailViewModel::toggleSaved
                )
            }
        }
    }
}


