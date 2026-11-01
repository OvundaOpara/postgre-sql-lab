package com.example.spring_task_api.services

import com.example.spring_task_api.models.CreateTaskRequest
import com.example.spring_task_api.models.Task
import com.example.spring_task_api.repositories.InMemoryTaskRepository
import org.springframework.stereotype.Service

@Service
class TaskService(private val repository: InMemoryTaskRepository) {

    fun getAllTasks(): List<Task> = repository.findAll()

    fun getTask(id: Long): Task? = repository.findById(id)

    fun createTask(request: CreateTaskRequest): Task {
        return repository.create(
            title = request.title,
            description = request.description,
            priority = request.priority
        )
    }

    fun updateTask(id: Long, request: CreateTaskRequest): Task? {
        return repository.update(
            id = id,
            title = request.title,
            description = request.description,
            priority = request.priority
        )
    }

    fun toggleTask(id: Long): Task? = repository.toggleCompleted(id)

    fun deleteTask(id: Long): Boolean = repository.delete(id)

    fun getCompletedTasks(): List<Task> = repository.findByCompleted(true)

    fun getPendingTasks(): List<Task> = repository.findByCompleted(false)

    fun getHighPriorityTasks(): List<Task> = repository.findByPriorityGreaterThan(3)
}