package com.devfinder.app.feature.home

data class RepositoryUiModel(
    val id: Long,
    val name: String,
    val owner: String,
    val description: String,
    val language: String,
    val stars: Int
)
