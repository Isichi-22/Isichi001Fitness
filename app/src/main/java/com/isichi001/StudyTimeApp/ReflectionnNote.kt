package com.isichi001.StudyTimeApp

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reflection_notes")
data class ReflectionNote(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val timestamp: Long = System.currentTimeMillis(),
    val moodIndex: Int,
    val content: String
)
