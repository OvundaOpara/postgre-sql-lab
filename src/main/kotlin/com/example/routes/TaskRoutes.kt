package com.example.routes

import com.example.models.Task
import com.example.services.TaskService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.taskRoutes(taskService: TaskService) {
    route("/api/tasks") {
        get {
            call.respond(taskService.getAllTasks())
        }
        get("/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "Invalid ID")
                return@get
            }
            val task = taskService.getTask(id)
            if (task == null) call.respond(HttpStatusCode.NotFound, "Task not found")
            else call.respond(task)
        }
        post {
            val task = call.receive<Task>()
            taskService.addTask(task)
            call.respond(HttpStatusCode.Created, "Task added")
        }
        put("/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "Invalid ID")
                return@put
            }
            val task = call.receive<Task>()
            val updated = taskService.updateTask(id, task)
            if (updated) call.respond(HttpStatusCode.OK, "Task updated")
            else call.respond(HttpStatusCode.NotFound, "Task not found")
        }
        delete("/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "Invalid ID")
                return@delete
            }
            val deleted = taskService.deleteTask(id)
            if (deleted) call.respond(HttpStatusCode.OK, "Task deleted")
            else call.respond(HttpStatusCode.NotFound, "Task not found")
        }
    }
}