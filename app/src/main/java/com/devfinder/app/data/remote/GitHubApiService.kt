package com.devfinder.app.data.remote

import com.devfinder.app.data.remote.dto.GitHubRepositoryDto
import com.devfinder.app.data.remote.dto.GitHubSearchResponseDto
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Path

interface GitHubApiService {

    @GET("search/repositories")
    suspend fun searchRepositories(
        @Query("q") query: String,
        @Query("sort") sort: String = "stars",
        @Query("order") order: String = "desc",
        @Query("per_page") perPage: Int = 20
    ): GitHubSearchResponseDto

    @GET("repositories/{id}")
    suspend fun getRepositoryById(
        @Path("id") repositoryId: Long
    ): GitHubRepositoryDto
}
