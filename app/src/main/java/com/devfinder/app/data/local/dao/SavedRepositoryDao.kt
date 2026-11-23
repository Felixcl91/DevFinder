package com.devfinder.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.devfinder.app.data.local.entity.SavedRepositoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SavedRepositoryDao {

    @Query("SELECT * FROM saved_repositories ORDER BY name ASC")
    fun observeSavedRepositories(): Flow<List<SavedRepositoryEntity>>

    @Query("SELECT EXISTS(SELECT 1 FROM saved_repositories WHERE id = :repositoryId)")
    fun observeIsSaved(repositoryId: Long): Flow<Boolean>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveRepository(repository: SavedRepositoryEntity)

    @Query("DELETE FROM saved_repositories WHERE id = :repositoryId")
    suspend fun deleteRepository(repositoryId: Long)

    @Query("SELECT * FROM saved_repositories WHERE id = :repositoryId LIMIT 1")
    suspend fun getSavedRepositoryById(
        repositoryId: Long
    ): SavedRepositoryEntity?

}
