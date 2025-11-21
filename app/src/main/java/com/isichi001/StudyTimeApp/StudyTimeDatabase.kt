package com.isichi001.StudyTimeApp

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Task::class], version = 1, exportSchema = false)
abstract class StudyTimeDatabase : RoomDatabase() {

    abstract fun taskDao(): TaskDao

    companion object {
        @Volatile
        private var INSTANCE: StudyTimeDatabase? = null

        fun getDatabase(context: Context): StudyTimeDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    StudyTimeDatabase::class.java,
                    "study_time_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
