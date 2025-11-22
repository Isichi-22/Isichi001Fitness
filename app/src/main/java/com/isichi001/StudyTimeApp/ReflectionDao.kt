package com.isichi001.StudyTimeApp

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ReflectionDao {

    // Returns the most recent reflection (or null if none saved yet)
    @Query("SELECT * FROM reflection_notes ORDER BY timestamp DESC LIMIT 1")
    fun getLatestReflection(): Flow<ReflectionNote?>

    // Inserts a new reflection into the database
    @Insert
    suspend fun insertReflection(note: ReflectionNote)
}
