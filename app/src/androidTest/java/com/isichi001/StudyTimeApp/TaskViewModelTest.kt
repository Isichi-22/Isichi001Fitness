package com.isichi001.StudyTimeApp

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertNotNull
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TaskViewModelTest {

    private lateinit var db: StudyTimeDatabase
    private lateinit var taskDao: TaskDao
    private lateinit var reflectionDao: ReflectionDao
    private lateinit var repository: TaskRepository
    private lateinit var viewModel: TaskViewModel

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()

        // In-memory DB just for tests
        db = Room.inMemoryDatabaseBuilder(
            context,
            StudyTimeDatabase::class.java
        )
            .allowMainThreadQueries()  // OK in tests
            .build()

        taskDao = db.taskDao()
        reflectionDao = db.reflectionDao()
        repository = TaskRepository(taskDao, reflectionDao)
        viewModel = TaskViewModel(repository)
    }

    @After
    fun tearDown() {
        db.close()
    }

    /**
     * ViewModel + Repository + Dao:
     * addTask() should end up inserting into the database.
     */
    @Test
    fun addTask_insertsTaskIntoDatabase() = runBlocking {
        // when: we ask the ViewModel to add a task
        viewModel.addTask(title = "VM Test task", plannedMinutes = 45)

        // give the viewModelScope coroutine a tiny bit of time to run
        delay(100)

        // then: the DAO should see that task in the DB
        val allTasks = taskDao.getAllTasks().first()

        assertEquals(1, allTasks.size)
        assertEquals("VM Test task", allTasks[0].title)
        assertEquals(45, allTasks[0].plannedMinutes)
        assertEquals(false, allTasks[0].isCompleted)
    }

    /**
     * ViewModel should be able to save a reflection via the Repository.
     */
    @Test
    fun saveReflection_savesLatestReflection() = runBlocking {
        // when: we save a reflection via the ViewModel
        viewModel.saveReflection(
            moodIndex = 4,
            content = "Feeling focused after a good study session"
        )

        // give coroutine a moment
        delay(100)

        // then: the ReflectionDao returns that note as latest
        val latest = reflectionDao.getLatestReflection().first()

        assertNotNull(latest)
        assertEquals(4, latest!!.moodIndex)
        assertEquals(
            "Feeling focused after a good study session",
            latest.content
        )
    }
}
