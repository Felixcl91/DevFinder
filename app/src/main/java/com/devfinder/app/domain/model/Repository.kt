package com.devfinder.app.domain.model

data class Repository(
    val id: Long,
    val name: String,
    val owner: String,
    val description: String,
    val language: String,
    val stars: Int
)
