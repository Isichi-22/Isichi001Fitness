package com.isichi001.StudyTimeApp

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [Task::class, ReflectionNote::class],
    version = 2,
    exportSchema = false
)
abstract class StudyTimeDatabase : RoomDatabase() {

    // DAOs that Room will generate implementations for
    abstract fun taskDao(): TaskDao
    abstract fun reflectionDao(): ReflectionDao

    companion object {
        @Volatile
        private var INSTANCE: StudyTimeDatabase? = null

        fun getDatabase(context: Context): StudyTimeDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    StudyTimeDatabase::class.java,
                    "study_time_database"
                )
                    // If schema changes (like we just added ReflectionNote),
                    // this wipes and recreates the DB instead of crashing.
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
