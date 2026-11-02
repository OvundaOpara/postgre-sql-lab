package com.example.auth

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import kotlinx.serialization.Serializable
import java.util.Date

@Serializable
data class LoginRequest(val username: String, val password: String)

@Serializable
data class LoginResponse(val token: String, val username: String, val role: String)

@Serializable
data class RegisterRequest(
    val username: String,
    val password: String
)

fun Route.authRoutes() {
    route("/api/auth") {
        post("/login") {
            val request = call.receive<LoginRequest>()
            val user = UserRepository.findByUsername(request.username)

            if (user == null || !UserRepository.validatePassword(user, request.password)) {
                call.respond(
                    HttpStatusCode.Unauthorized,
                    mapOf("error" to "Invalid username or password")
                )
                return@post
            }

            val token = JWT.create()
                .withIssuer(JwtConfig.issuer)
                .withAudience(JwtConfig.audience)
                .withClaim("username", user.username)
                .withClaim("userId", user.id)
                .withClaim("role", user.role)
                .withExpiresAt(Date(System.currentTimeMillis() + JwtConfig.expirationMs))
                .sign(Algorithm.HMAC256(JwtConfig.secret))

            call.respond(LoginResponse(token, user.username, user.role))
        }
    }
}