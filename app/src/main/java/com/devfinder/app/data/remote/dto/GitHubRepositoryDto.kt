package com.devfinder.app.data.remote.dto

import com.google.gson.annotations.SerializedName

data class GitHubRepositoryDto(
    val id: Long,
    val name: String,
    val description: String?,
    val language: String?,

    @SerializedName("stargazers_count")
    val stars: Int,

    val owner: GitHubOwnerDto
)

data class GitHubOwnerDto(
    val login: String
)

data class GitHubSearchResponseDto(
    val items: List<GitHubRepositoryDto>
)
