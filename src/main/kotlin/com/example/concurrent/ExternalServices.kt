package com.example.concurrent

import kotlinx.coroutines.delay

object ExternalServices {

    suspend fun fetchUserProfile(userId: Int): UserProfile {
        delay(500) // simulate network call
        return UserProfile(userId, "User $userId", "user$userId@example.com")
    }

    suspend fun fetchOrders(userId: Int): List<Order> {
        delay(800) // simulate network call
        return listOf(
            Order(1, userId, 99.99),
            Order(2, userId, 149.50),
            Order(3, userId, 25.00)
        )
    }

    suspend fun fetchRecommendations(userId: Int): List<Recommendation> {
        delay(600) // simulate network call
        return listOf(
            Recommendation(userId, listOf("Product A", "Product B", "Product C"))
        )
    }
}