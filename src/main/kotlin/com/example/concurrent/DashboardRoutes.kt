package com.example.concurrent

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.dashboardRoutes(dashboardService: DashboardService) {
    get("/api/dashboard/{userId}") {
        val userId = call.parameters["userId"]?.toIntOrNull()
        if (userId == null) {
            call.respond(
                io.ktor.http.HttpStatusCode.BadRequest,
                mapOf("error" to "Invalid user ID")
            )
            return@get
        }

        val dashboard = dashboardService.getUserDashboard(userId)
        call.respond(dashboard)
    }
}