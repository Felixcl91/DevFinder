package com.devfinder.app.data.mapper

import com.devfinder.app.data.local.entity.SavedRepositoryEntity
import com.devfinder.app.domain.model.Repository
import org.junit.Assert.assertEquals
import org.junit.Test

class SavedRepositoryMapperTest {

    @Test
    fun `repository converts to Room entity`() {
        val repository = Repository(
            id = 123L,
            name = "DevFinder",
            owner = "developer",
            description = "Android application",
            language = "Kotlin",
            stars = 150
        )

        val entity = repository.toEntity()

        assertEquals(repository.id, entity.id)
        assertEquals(repository.name, entity.name)
        assertEquals(repository.owner, entity.owner)
        assertEquals(repository.description, entity.description)
        assertEquals(repository.language, entity.language)
        assertEquals(repository.stars, entity.stars)
    }

    @Test
    fun `Room entity converts to domain repository`() {
        val entity = SavedRepositoryEntity(
            id = 456L,
            name = "ComposeApp",
            owner = "androiddev",
            description = "Jetpack Compose example",
            language = "Kotlin",
            stars = 320
        )

        val repository = entity.toDomain()

        assertEquals(entity.id, repository.id)
        assertEquals(entity.name, repository.name)
        assertEquals(entity.owner, repository.owner)
        assertEquals(entity.description, repository.description)
        assertEquals(entity.language, repository.language)
        assertEquals(entity.stars, repository.stars)
    }
}
