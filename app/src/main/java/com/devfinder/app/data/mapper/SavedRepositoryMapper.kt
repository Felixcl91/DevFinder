package com.devfinder.app.data.mapper

import com.devfinder.app.data.local.entity.SavedRepositoryEntity
import com.devfinder.app.domain.model.Repository

fun Repository.toEntity(): SavedRepositoryEntity {
    return SavedRepositoryEntity(
        id = id,
        name = name,
        owner = owner,
        description = description,
        language = language,
        stars = stars
    )
}

fun SavedRepositoryEntity.toDomain(): Repository {
    return Repository(
        id = id,
        name = name,
        owner = owner,
        description = description,
        language = language,
        stars = stars
    )
}
