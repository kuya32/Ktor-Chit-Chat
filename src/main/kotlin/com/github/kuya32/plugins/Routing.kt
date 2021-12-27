package com.github.kuya32.plugins

import com.github.kuya32.repository.follow.FollowRepository
import com.github.kuya32.repository.user.UserRepository
import com.github.kuya32.routes.createUser
import com.github.kuya32.routes.followUser
import com.github.kuya32.routes.loginUser
import com.github.kuya32.routes.unfollowUser
import com.github.kuya32.service.UserService
import io.ktor.routing.*
import io.ktor.application.*
import org.koin.ktor.ext.inject

fun Application.configureRouting() {
    val userService: UserService by inject()
    val followRepository: FollowRepository by inject()
    routing {
        // User routes
        createUser(userService)
//        loginUser(userService)

        // Following routes
        followUser(followRepository)
        unfollowUser(followRepository)
    }
}
