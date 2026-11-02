package com.example

import com.example.auth.authRoutes
import com.example.auth.configureJwt
import com.example.concurrent.DashboardService
import com.example.concurrent.dashboardRoutes
import com.example.database.DatabaseFactory
import com.example.repositories.PostgresTaskRepository
import com.example.routes.taskRoutes
import com.example.services.TaskService
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun main() {
    DatabaseFactory.init()

    val repository = PostgresTaskRepository()
    val taskService = TaskService(repository)
    val dashboardService = DashboardService()

    embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
        install(ContentNegotiation) { json() }
        configureJwt()

        routing {
            // Public health checkpoint
            get("/health") {
                call.respond(mapOf("status" to "UP"))
            }

            authRoutes()
            
            // Protected routes requiring a JWT token
            authenticate("auth-jwt") {
                taskRoutes(taskService)
                dashboardRoutes(dashboardService)
            }
        }
    }.start(wait = true)
}