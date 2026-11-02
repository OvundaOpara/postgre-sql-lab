package com.example.routes

import com.example.models.Task
import com.example.models.TaskRepository
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.routing.*
import io.ktor.server.testing.*
import kotlin.test.*
import java.time.LocalDateTime

class TaskRoutesTest {

    class StubTaskRepository : TaskRepository {
        private val tasks = mutableListOf<Task>()
        private var nextId = 1

        override fun findAll(): List<Task> = tasks
        override fun findById(id: Int): Task? = tasks.find { it.id == id }
        override fun create(title: String, description: String, priority: Int): Task {
            val t = Task(nextId++, title, description, false, priority, LocalDateTime.now())
            tasks.add(t)
            return t
        }
        override fun update(id: Int, title: String, description: String, priority: Int): Task? {
            val index = tasks.indexOfFirst { it.id == id }
            if (index == -1) return null
            val updated = tasks[index].copy(title = title, description = description, priority = priority)
            tasks[index] = updated
            return updated
        }
        override fun toggleCompleted(id: Int): Task? {
            val index = tasks.indexOfFirst { it.id == id }
            if (index == -1) return null
            val updated = tasks[index].copy(completed = !tasks[index].completed)
            tasks[index] = updated
            return updated
        }
        override fun delete(id: Int): Boolean = tasks.removeIf { it.id == id }
        override fun findByCompleted(completed: Boolean): List<Task> = emptyList()
        override fun findByPriorityGreaterThan(priority: Int): List<Task> = emptyList()
    }

    private fun ApplicationTestBuilder.setupApplication() {
        application {
            install(ContentNegotiation) { json() }
            val service = TaskService(StubTaskRepository())
            routing {
                taskRoutes(service)
            }
        }
    }

    @Test
    fun `GET all tasks should return empty list initially`() = testApplication {
        setupApplication()
        val response = client.get("/api/tasks")
        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals("[]", response.bodyAsText())
    }

    @Test
    fun `POST task should create task and return 201`() = testApplication {
        setupApplication()
        val response = client.post("/api/tasks") {
            contentType(ContentType.Application.Json)
            setBody("""{"title":"Test Task","description":"Test Description","priority":3}""")
        }
        assertEquals(HttpStatusCode.Created, response.status)
        assertTrue(response.bodyAsText().contains("Test Task"))
    }

    @Test
    fun `GET task by id should return 404 when not found`() = testApplication {
        setupApplication()
        val response = client.get("/api/tasks/999")
        assertEquals(HttpStatusCode.NotFound, response.status)
    }
}