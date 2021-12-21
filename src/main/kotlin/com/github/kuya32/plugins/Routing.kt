package com.github.kuya32.plugins

import com.github.kuya32.repository.user.UserRepository
import com.github.kuya32.routes.createUser
import io.ktor.routing.*
import io.ktor.application.*
import org.koin.ktor.ext.inject

fun Application.configureRouting() {
    val userRepository: UserRepository by inject()
    routing {
        createUser(userRepository)
    }
}
