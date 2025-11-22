package com.isichi001.StudyTimeApp

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertTrue
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TaskDaoTest {

    private lateinit var db: StudyTimeDatabase
    private lateinit var dao: TaskDao

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context,
            StudyTimeDatabase::class.java
        )
            .allowMainThreadQueries()   // ok for tests
            .build()

        dao = db.taskDao()
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun insertTask_andReadItBack() = runBlocking {
        val task = Task(
            title = "Test task",
            plannedMinutes = 30,
            isCompleted = false
        )

        dao.insertTask(task)

        val allTasks = dao.getAllTasks().first()

        assertEquals(1, allTasks.size)
        assertEquals("Test task", allTasks[0].title)
        assertEquals(30, allTasks[0].plannedMinutes)
        assertTrue(!allTasks[0].isCompleted)
    }
}
