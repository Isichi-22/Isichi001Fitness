package com.isichi001.StudyTimeApp

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ReflectionDaoTest {

    private lateinit var db: StudyTimeDatabase
    private lateinit var reflectionDao: ReflectionDao

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context,
            StudyTimeDatabase::class.java
        )
            .allowMainThreadQueries() // fine for tests
            .build()

        reflectionDao = db.reflectionDao()
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun insertReflection_andReadItBack() = runBlocking {
        // given: one reflection note
        val note = ReflectionNote(
            moodIndex = 3,
            content = "Test reflection note"
        )

        // when: we insert it
        reflectionDao.insertReflection(note)

        // and then read whatever getLatestReflection() returns
        val latest = reflectionDao.getLatestReflection().first()

        // then: it should not be null and should match what we saved
        assertNotNull(latest)
        assertEquals(3, latest!!.moodIndex)
        assertEquals("Test reflection note", latest.content)
    }
}
