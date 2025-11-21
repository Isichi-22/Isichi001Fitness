package com.isichi001.StudyTimeApp

import kotlinx.coroutines.flow.Flow

class TaskRepository(private val taskDao: TaskDao) {

    // A stream of all tasks from the database.
    // Your UI can observe this and update automatically when data changes.
    val allTasks: Flow<List<Task>> = taskDao.getAllTasks()

    // Add a new task
    suspend fun addTask(task: Task) {
        taskDao.insertTask(task)
    }

    // Update an existing task (e.g. mark it complete)
    suspend fun updateTask(task: Task) {
        taskDao.updateTask(task)
    }

    // Delete a single task
    suspend fun deleteTask(task: Task) {
        taskDao.deleteTask(task)
    }

    // Delete everything (e.g. clear list)
    suspend fun deleteAllTasks() {
        taskDao.deleteAllTasks()
    }
}
