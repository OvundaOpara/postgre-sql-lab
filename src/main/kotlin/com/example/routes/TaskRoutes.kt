package com.example.routes

import com.example.models.TaskRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable

@Serializable
data class CreateTaskRequest(val title: String, val description: String, val priority: Int)

@Serializable
data class UpdateTaskRequest(val title: String, val description: String, val priority: Int)

class TaskService(private val repository: TaskRepository) {
    fun getAllTasks() = repository.findAll()
    fun getTaskById(id: Int) = repository.findById(id)
    fun createTask(req: CreateTaskRequest) = repository.create(req.title, req.description, req.priority)
    fun updateTask(id: Int, req: UpdateTaskRequest) = repository.update(id, req.title, req.description, req.priority)
    fun toggleTask(id: Int) = repository.toggleCompleted(id)
    fun deleteTask(id: Int) = repository.delete(id)
}

fun Route.taskRoutes(taskService: TaskService) {
    route("/api/tasks") {
        get { call.respond(taskService.getAllTasks()) }
        post {
            val req = call.receive<CreateTaskRequest>()
            call.respond(HttpStatusCode.Created, taskService.createTask(req))
        }
        get("{id}") {
            val id = call.parameters["id"]?.toIntOrNull() ?: return@get call.respond(HttpStatusCode.BadRequest)
            val task = taskService.getTaskById(id) ?: return@get call.respond(HttpStatusCode.NotFound)
            call.respond(task)
        }
        put("{id}") {
            val id = call.parameters["id"]?.toIntOrNull() ?: return@put call.respond(HttpStatusCode.BadRequest)
            val req = call.receive<UpdateTaskRequest>()
            val task = taskService.updateTask(id, req) ?: return@put call.respond(HttpStatusCode.NotFound)
            call.respond(task)
        }
        patch("{id}/toggle") {
            val id = call.parameters["id"]?.toIntOrNull() ?: return@patch call.respond(HttpStatusCode.BadRequest)
            val task = taskService.toggleTask(id) ?: return@patch call.respond(HttpStatusCode.NotFound)
            call.respond(task)
        }
        delete("{id}") {
            val id = call.parameters["id"]?.toIntOrNull() ?: return@delete call.respond(HttpStatusCode.BadRequest)
            if (taskService.deleteTask(id)) call.respond(HttpStatusCode.NoContent)
            else call.respond(HttpStatusCode.NotFound)
        }
    }
}