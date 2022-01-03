package com.github.kuya32.routes

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.github.kuya32.data.models.User
import com.github.kuya32.repository.user.UserRepository
import com.github.kuya32.data.requests.CreateAccountRequest
import com.github.kuya32.data.requests.LoginRequest
import com.github.kuya32.data.responses.AuthResponse
import com.github.kuya32.data.responses.BasicApiResponse
import com.github.kuya32.service.UserService
import com.github.kuya32.util.ApiResponseMessages.FIELDS_BLANK
import com.github.kuya32.util.ApiResponseMessages.INVALID_CREDENTIALS
import com.github.kuya32.util.ApiResponseMessages.USER_ALREADY_EXISTS
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import org.koin.ktor.ext.get
import java.util.*

fun Route.createUser(userService: UserService) {
    post("/api/user/create") {
        val request = call.receiveOrNull<CreateAccountRequest>() ?: kotlin.run {
            call.respond(HttpStatusCode.BadRequest)
            return@post
        }

        if (userService.doesUserWithEmailExist(request.email)) {
            call.respond(
                BasicApiResponse<Unit>(false, USER_ALREADY_EXISTS)
            )
            return@post
        }
        when (userService.validateCreatedAccountRequest(request)) {
            is UserService.ValidateEvent.ErrorFieldEmpty -> {
                call.respond(
                    BasicApiResponse<Unit>(false, FIELDS_BLANK)
                )
            }
            is UserService.ValidateEvent.Success -> {
                userService.createUser(request)
                call.respond(
                    BasicApiResponse<Unit>(true)
                )
            }
        }
    }
}

fun Route.loginUser(
    userService: UserService,
    jwtIssuer: String,
    jwtAudience: String,
    jwtSecret: String
) {
    post("/api/user/login") {
        val request = call.receiveOrNull<LoginRequest>() ?: kotlin.run {
            call.respond(HttpStatusCode.BadRequest)
            return@post
        }

        if (request.email.isBlank() || request.password.isBlank()) {
            call.respond(HttpStatusCode.BadRequest)
            return@post
        }

        val user = userService.getUserByEmail(request.email) ?: kotlin.run {
            call.respond(
                HttpStatusCode.OK,
                BasicApiResponse<Unit>(
                    successful = false,
                    message = INVALID_CREDENTIALS
                )
            )
            return@post
        }

        val isCorrectPassword = userService.isValidPassword(
            enteredPassword = request.password,
            actualPassword = user.password
        )
        if (isCorrectPassword) {
            val expiresIn = 1000L * 60L * 60L * 24L * 265L
            val token = JWT.create()
                .withClaim("userId", user.id)
                .withIssuer(jwtIssuer)
                .withExpiresAt(Date(System.currentTimeMillis() + expiresIn))
                .withAudience(jwtAudience)
                .sign(Algorithm.HMAC256(jwtSecret))
            call.respond(
                HttpStatusCode.OK,
                BasicApiResponse(
                    successful = true,
                    data = AuthResponse(
                        userId = user.id,
                        token = token
                    )
                )
            )
        } else {
            call.respond(
                HttpStatusCode.OK,
                BasicApiResponse<Unit>(
                    successful = false,
                    message = INVALID_CREDENTIALS
                )
            )
        }
    }
}

fun Route.authenticate() {
    authenticate {
        get("/api/user/authenticate") {
            call.respond(HttpStatusCode.OK)
        }
    }
}