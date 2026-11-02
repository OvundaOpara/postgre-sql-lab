package com.example.routes

import com.example.models.Task
import com.example.models.TaskRepository
import io.mockk.*
import kotlin.test.*
import java.time.LocalDateTime

class TaskServiceTest {
    private lateinit var repository: TaskRepository
    private lateinit var service: TaskService

    @BeforeTest
    fun setup() {
        repository = mockk()
        service = TaskService(repository)
    }

    @Test
    fun `createTask should return task when input is valid`() {
        val request = CreateTaskRequest("Learn Testing", "Write tests", 3)
        val expectedTask = Task(1, "Learn Testing", "Write tests", false, 3, LocalDateTime.now())

        every { repository.create(any(), any(), any()) } returns expectedTask

        val result = service.createTask(request)

        assertEquals(expectedTask, result)
        verify(exactly = 1) { repository.create("Learn Testing", "Write tests", 3) }
    }

    @Test
    fun `getTaskById should return task when task exists`() {
        val task = Task(1, "Existing Task", "Description", false, 3, LocalDateTime.now())
        every { repository.findById(1) } returns task

        val result = service.getTaskById(1)

        assertNotNull(result)
        assertEquals(task, result)
    }

    @Test
    fun `getTaskById should return null when task does not exist`() {
        every { repository.findById(999) } returns null

        val result = service.getTaskById(999)

        assertNull(result)
    }

    @Test
    fun `deleteTask should return false when task does not exist`() {
        every { repository.delete(999) } returns false

        val result = service.deleteTask(999)

        assertFalse(result)
    }

    @Test
    fun `deleteTask should return true when task is deleted`() {
        every { repository.delete(1) } returns true

        val result = service.deleteTask(1)

        assertTrue(result)
    }
}