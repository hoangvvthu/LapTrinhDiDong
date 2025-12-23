package com.example.baitap11.repository

import com.example.baitap11.data.TaskDao
import com.example.baitap11.model.Task
import kotlinx.coroutines.flow.Flow

class TaskRepository(private val taskDao: TaskDao) {
    fun getTasksByUser(userId: Int): Flow<List<Task>> = taskDao.getTasksByUser(userId)
    suspend fun insert(task: Task): Long = taskDao.insert(task)
    suspend fun update(task: Task) = taskDao.update(task)
    suspend fun delete(task: Task) = taskDao.delete(task)
}
