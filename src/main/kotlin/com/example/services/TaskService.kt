package com.example.services

import com.example.models.Task
import com.example.repositories.PostgresTaskRepository
import com.github.benmanes.caffeine.cache.Caffeine
import java.util.concurrent.TimeUnit

class TaskService(private val repository: PostgresTaskRepository) {

    private val taskCache = Caffeine.newBuilder()
        .expireAfterWrite(5, TimeUnit.MINUTES)
        .maximumSize(100)
        .build<Int, Task>()

    fun getAllTasks(): List<Task> {
        return repository.getAllTasks()
    }

    fun getTask(id: Int): Task? {
        return taskCache.get(id) { 
            repository.getTaskById(id) 
        }
    }

    fun addTask(task: Task) {
        repository.addTask(task)
    }

    fun updateTask(id: Int, task: Task): Boolean {
        val updated = repository.updateTask(id, task)
        if (updated) {
            taskCache.put(id, task) 
        }
        return updated
    }

    fun deleteTask(id: Int): Boolean {
        val deleted = repository.deleteTask(id)
        if (deleted) {
            taskCache.invalidate(id) 
        }
        return deleted
    }
}