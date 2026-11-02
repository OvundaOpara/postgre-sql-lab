package com.example

import com.example.auth.authRoutes
import com.example.auth.configureJwt
import com.example.concurrent.DashboardService
import com.example.concurrent.dashboardRoutes
import com.example.database.DatabaseFactory
import com.example.repositories.PostgresTaskRepository
import com.example.routes.TaskService
import com.example.routes.taskRoutes
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.routing.*

fun main() {
    DatabaseFactory.init()

    val repository = PostgresTaskRepository()
    val taskService = TaskService(repository)
    val dashboardService = DashboardService()

    embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
        install(ContentNegotiation) { json() }
        
        configureJwt() // Initialize JWT Authentication

        routing {
            authRoutes() // Public endpoint for logging in

            authenticate("auth-jwt") {
                // Everything inside here now requires a valid JWT token
                taskRoutes(taskService)
                dashboardRoutes(dashboardService)
            }
        }
    }.start(wait = true)
}