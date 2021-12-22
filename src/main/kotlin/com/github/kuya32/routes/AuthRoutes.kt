package com.github.kuya32.routes

import com.github.kuya32.data.models.User
import com.github.kuya32.repository.user.UserRepository
import com.github.kuya32.data.requests.CreateAccountRequest
import com.github.kuya32.data.requests.LoginRequest
import com.github.kuya32.data.responses.BasicApiResponse
import com.github.kuya32.util.ApiResponseMessages.FIELDS_BLANK
import com.github.kuya32.util.ApiResponseMessages.INVALID_CREDENTIALS
import com.github.kuya32.util.ApiResponseMessages.USER_ALREADY_EXISTS
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.createUser(userRepository: UserRepository) {
    post("/api/user/create") {
        val request = call.receiveOrNull<CreateAccountRequest>() ?: kotlin.run {
            call.respond(HttpStatusCode.BadRequest)
            return@post
        }
        val userExist = userRepository.getUserByEmail(request.email) != null
        if (userExist) {
            call.respond(
                BasicApiResponse(false, USER_ALREADY_EXISTS)
            )
            return@post
        }
        if (request.email.isBlank() || request.password.isBlank() || request.username.isBlank
                ()) {
            call.respond(
                BasicApiResponse(false, FIELDS_BLANK)
            )
            return@post
        }
        userRepository.createdUser(
            User(
                email = request.email,
                username = request.username,
                password = request.password,
                profileImageUrl = "",
                bio = "",
                githubUrl = null,
                instagramUrl = null,
                linkedInUrl = null
            )
        )
        call.respond(
            BasicApiResponse(true)
        )
    }
}

fun Route.loginUser(userRepository: UserRepository) {
    post("/api/user/login") {
        val request = call.receiveOrNull<LoginRequest>() ?: kotlin.run {
            call.respond(HttpStatusCode.BadRequest)
            return@post
        }

        if (request.email.isBlank() || request.password.isBlank()) {
            call.respond(HttpStatusCode.BadRequest)
            return@post
        }

        val isCorrectPassword = userRepository.doesPasswordForUserMatch(
            email = request.email,
            enteredPassword = request.password
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