package com.devfinder.app.core.navigation

object DevFinderRoute {
    const val HOME = "home"
    const val SEARCH = "search"
    const val SAVED = "saved"
    const val PROFILE = "profile"

    const val DETAIL = "detail/{repositoryId}"

    fun detail(repositoryId: Long): String {
        return "detail/$repositoryId"
    }
}
