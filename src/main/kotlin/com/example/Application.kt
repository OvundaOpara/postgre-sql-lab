package com.example

import com.example.database.DatabaseFactory
import com.example.repositories.PostgresTaskRepository
import com.example.routes.TaskService
import com.example.routes.taskRoutes
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.routing.*

fun main() {
    DatabaseFactory.init()

    val repository = PostgresTaskRepository()
    val service = TaskService(repository)

    embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
        install(ContentNegotiation) { json() }
        routing {
            taskRoutes(service)
        }
    }.start(wait = true)
}