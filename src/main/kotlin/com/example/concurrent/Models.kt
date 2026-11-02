package com.example.concurrent

import kotlinx.serialization.Serializable

@Serializable
data class UserProfile(
    val userId: Int,
    val name: String,
    val email: String
)

@Serializable
data class Order(
    val orderId: Int,
    val userId: Int,
    val amount: Double
)

@Serializable
data class Recommendation(
    val userId: Int,
    val products: List<String>
)

@Serializable
data class UserDashboard(
    val profile: UserProfile,
    val orders: List<Order>,
    val recommendations: List<Recommendation>,
    val totalSpent: Double
)