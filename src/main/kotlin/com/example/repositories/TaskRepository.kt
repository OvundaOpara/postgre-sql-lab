package com.example.repositories

import com.example.models.Task

interface TaskRepository {
    fun getAllTasks(): List<Task>
    fun getTaskById(id: Int): Task?
    fun addTask(task: Task)
    fun updateTask(id: Int, task: Task): Boolean
    fun deleteTask(id: Int): Boolean
}