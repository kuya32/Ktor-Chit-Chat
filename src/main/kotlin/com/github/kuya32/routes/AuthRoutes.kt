package com.github.kuya32.routes

import com.github.kuya32.data.models.User
import com.github.kuya32.repository.user.UserRepository
import com.github.kuya32.data.requests.CreateAccountRequest
import com.github.kuya32.data.requests.LoginRequest
import com.github.kuya32.data.responses.BasicApiResponse
import com.github.kuya32.service.UserService
import com.github.kuya32.util.ApiResponseMessages.FIELDS_BLANK
import com.github.kuya32.util.ApiResponseMessages.INVALID_CREDENTIALS
import com.github.kuya32.util.ApiResponseMessages.USER_ALREADY_EXISTS
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.createUser(userService: UserService) {
    post("/api/user/create") {
        val request = call.receiveOrNull<CreateAccountRequest>() ?: kotlin.run {
            call.respond(HttpStatusCode.BadRequest)
            return@post
        }

        if (userService.doesUserWithEmailExist(request.email)) {
            call.respond(
                BasicApiResponse(false, USER_ALREADY_EXISTS)
            )
            return@post
        }
        when (userService.validateCreatedAccountRequest(request)) {
            is UserService.ValidateEvent.ErrorFieldEmpty -> {
                call.respond(
                    BasicApiResponse(false, FIELDS_BLANK)
                )
            }
            is UserService.ValidateEvent.Success -> {
                userService.createUser(request)
                call.respond(
                    BasicApiResponse(true)
                )
            }
        }
    }
}

fun Route.loginUser(
    userService: UserService,
    jwtIssuer: String,
    
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
                BasicApiResponse(
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
            call.respond(
                HttpStatusCode.OK,
                BasicApiResponse(
                    successful = true
                )
            )
        } else {
            call.respond(
                HttpStatusCode.OK,
                BasicApiResponse(
                    successful = false,
                    message = INVALID_CREDENTIALS
                )
            )
        }
    }
}