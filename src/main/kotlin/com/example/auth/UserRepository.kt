package com.example.auth

import org.mindrot.jbcrypt.BCrypt

data class User(
    val id: Int,
    val username: String,
    val passwordHash: String,
    val role: String = "USER"
)

object UserRepository {
    private val users = mutableListOf(
        User(1, "admin", BCrypt.hashpw("admin123", BCrypt.gensalt()), "ADMIN"),
        User(2, "alice", BCrypt.hashpw("alice123", BCrypt.gensalt()), "USER"),
        User(3, "bob", BCrypt.hashpw("bob123", BCrypt.gensalt()), "USER")
    )

    fun findByUsername(username: String): User? =
        users.find { it.username == username }

    fun findById(id: Int): User? = users.find { it.id == id }

    fun validatePassword(user: User, password: String): Boolean =
        BCrypt.checkpw(password, user.passwordHash)
}