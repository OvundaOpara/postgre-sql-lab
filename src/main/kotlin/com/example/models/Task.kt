package com.example.models

import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class Task(
    val id: Int,
    val title: String,
    val description: String,
    val completed: Boolean = false,
    val priority: Int = 3,
    @Serializable(with = LocalDateTimeSerializer::class)
    val createdAt: LocalDateTime = LocalDateTime.now()
)

interface TaskRepository {
    fun findAll(): List<Task>
    fun findById(id: Int): Task?
    fun create(title: String, description: String, priority: Int): Task
    fun update(id: Int, title: String, description: String, priority: Int): Task?
    fun toggleCompleted(id: Int): Task?
    fun delete(id: Int): Boolean
    fun findByCompleted(completed: Boolean): List<Task>
    fun findByPriorityGreaterThan(priority: Int): List<Task>
}