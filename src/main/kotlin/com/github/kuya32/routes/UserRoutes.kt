package com.github.kuya32.routes

import com.github.kuya32.data.repository.user.UserRepository
import com.github.kuya32.data.requests.CreateAccountRequest
import com.github.kuya32.data.responses.BasicApiResponse
import com.github.kuya32.util.ApiResponseMessages
import com.github.kuya32.util.ApiResponseMessages.FIELDS_BLANK
import com.github.kuya32.util.ApiResponseMessages.USER_ALREADY_EXISTS
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import org.koin.ktor.ext.inject

fun Route.userRoutes() {
    val userRepository: UserRepository by inject()
    route("/api/user/create") {
        post {
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
            call.respond(
                BasicApiResponse(true)
            )
        }
    }
}