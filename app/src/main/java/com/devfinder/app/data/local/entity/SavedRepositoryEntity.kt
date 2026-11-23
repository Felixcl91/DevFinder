package com.devfinder.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "saved_repositories")
data class SavedRepositoryEntity(
    @PrimaryKey
    val id: Long,
    val name: String,
    val owner: String,
    val description: String,
    val language: String,
    val stars: Int
)
