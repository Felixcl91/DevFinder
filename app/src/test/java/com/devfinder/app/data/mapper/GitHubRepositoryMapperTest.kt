package com.devfinder.app.data.mapper

import com.devfinder.app.data.remote.dto.GitHubOwnerDto
import com.devfinder.app.data.remote.dto.GitHubRepositoryDto
import org.junit.Assert.assertEquals
import org.junit.Test

class GitHubRepositoryMapperTest {

    @Test
    fun `toDomain maps repository correctly`() {
        val dto = GitHubRepositoryDto(
            id = 123L,
            name = "devfinder",
            description = "Android portfolio app",
            language = "Kotlin",
            stars = 1500,
            owner = GitHubOwnerDto(login = "developer")
        )

        val result = dto.toDomain()

        assertEquals(123L, result.id)
        assertEquals("devfinder", result.name)
        assertEquals("developer", result.owner)
        assertEquals("Android portfolio app", result.description)
        assertEquals("Kotlin", result.language)
        assertEquals(1500, result.stars)
    }

    @Test
    fun `toDomain handles null fields`() {
        val dto = GitHubRepositoryDto(
            id = 456L,
            name = "sample",
            description = null,
            language = null,
            stars = 0,
            owner = GitHubOwnerDto(login = "developer")
        )

        val result = dto.toDomain()

        assertEquals("", result.description)
        assertEquals("Unknown", result.language)
    }
}
