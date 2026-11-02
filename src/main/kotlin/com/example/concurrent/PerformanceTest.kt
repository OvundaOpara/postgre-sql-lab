package com.example.concurrent

import kotlinx.coroutines.runBlocking
import kotlin.system.measureTimeMillis

fun main() = runBlocking {
    val service = DashboardService()

    println("Fetching dashboard for user 1...")
    val time = measureTimeMillis {
        val dashboard = service.getUserDashboard(1)
        println("Profile: ${dashboard.profile.name}")
        println("Orders: ${dashboard.orders.size}")
        println("Recommendations: ${dashboard.recommendations.size}")
        println("Total spent: ${dashboard.totalSpent}")
    }
    println("Time taken: ${time}ms")

    println()
    println("Fetching dashboards for users 1-5 concurrently...")
    val batchTime = measureTimeMillis {
        val dashboards = service.getMultipleDashboards((1..5).toList())
        println("Fetched ${dashboards.size} dashboards")
    }
    println("Time taken: ${batchTime}ms")
}