package com.example.spring_task_api.models

import jakarta.validation.constraints.*
import java.time.LocalDateTime

data class Task(
    val id: Long? = null,

    @field:NotBlank(message = "Title is required")
    @field:Size(max = 100, message = "Title cannot exceed 100 characters")
    val title: String,

    @field:Size(max = 1000, message = "Description cannot exceed 1000 characters")
    val description: String = "",

    val completed: Boolean = false,

    @field:Min(value = 1, message = "Priority must be at least 1")
    @field:Max(value = 5, message = "Priority must be at most 5")
    val priority: Int = 3,

    val createdAt: LocalDateTime = LocalDateTime.now()
)

data class CreateTaskRequest(
    @field:NotBlank(message = "Title is required")
    @field:Size(max = 100, message = "Title cannot exceed 100 characters")
    val title: String,

    @field:Size(max = 1000, message = "Description cannot exceed 1000 characters")
    val description: String = "",

    @field:Min(value = 1, message = "Priority must be at least 1")
    @field:Max(value = 5, message = "Priority must be at most 5")
    val priority: Int = 3
)