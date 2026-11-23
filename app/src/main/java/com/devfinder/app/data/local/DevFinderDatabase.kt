package com.devfinder.app.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.devfinder.app.data.local.dao.SavedRepositoryDao
import com.devfinder.app.data.local.entity.SavedRepositoryEntity

@Database(
    entities = [SavedRepositoryEntity::class],
    version = 1,
    exportSchema = false
)
abstract class DevFinderDatabase : RoomDatabase() {

    abstract fun savedRepositoryDao(): SavedRepositoryDao
}
