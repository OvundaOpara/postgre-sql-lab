package com.example.spring_task_api.repositories

import com.example.spring_task_api.models.Task
import org.springframework.stereotype.Repository
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicLong

@Repository
class InMemoryTaskRepository {
    private val tasks = ConcurrentHashMap<Long, Task>()
    private val nextId = AtomicLong(1)

    fun findAll(): List<Task> = tasks.values.toList()

    fun findById(id: Long): Task? = tasks[id]

    fun create(title: String, description: String, priority: Int): Task {
        val task = Task(
            id = nextId.getAndIncrement(),
            title = title,
            description = description,
            priority = priority
        )
        tasks[task.id!!] = task
        return task
    }

    fun update(id: Long, title: String, description: String, priority: Int): Task? {
        val existing = tasks[id] ?: return null
        val updated = existing.copy(
            title = title,
            description = description,
            priority = priority
        )
        tasks[id] = updated
        return updated
    }

    fun toggleCompleted(id: Long): Task? {
        val existing = tasks[id] ?: return null
        val updated = existing.copy(completed = !existing.completed)
        tasks[id] = updated
        return updated
    }

    fun delete(id: Long): Boolean = tasks.remove(id) != null

    fun findByCompleted(completed: Boolean): List<Task> =
        tasks.values.filter { it.completed == completed }

    fun findByPriorityGreaterThan(priority: Int): List<Task> =
        tasks.values.filter { it.priority > priority }
}