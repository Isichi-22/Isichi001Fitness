package com.isichi001.StudyTimeApp

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val title: String,
    val plannedMinutes: Int,
    val isCompleted: Boolean = false
)
