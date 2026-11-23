package com.devfinder.app.data.mapper

import com.devfinder.app.data.remote.dto.GitHubRepositoryDto
import com.devfinder.app.domain.model.Repository

fun GitHubRepositoryDto.toDomain(): Repository {
    return Repository(
        id = id,
        name = name,
        owner = owner.login,
        description = description.orEmpty(),
        language = language ?: "Unknown",
        stars = stars
    )
}
