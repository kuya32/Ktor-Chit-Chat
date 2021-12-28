package com.github.kuya32.plugins

import com.github.kuya32.repository.follow.FollowRepository
import com.github.kuya32.repository.user.UserRepository
import com.github.kuya32.routes.createUser
import com.github.kuya32.routes.followUser
import com.github.kuya32.routes.loginUser
import com.github.kuya32.routes.unfollowUser
import com.github.kuya32.service.FollowService
import com.github.kuya32.service.UserService
import io.ktor.routing.*
import io.ktor.application.*
import org.koin.ktor.ext.inject

fun Application.configureRouting() {
    val userService: UserService by inject()
    val followService: FollowService by inject()

    val jwtIssuer = environment.config.property("jwt.domain").toString()
    val jwtAudience = environment.config.property("jwt.audience").toString()
    val jwtSecret = environment.config.property("jwt.secret").toString()
    routing {
        // User routes
        createUser(userService)
        loginUser(
            userService = userService,
            jwtIssuer = jwtIssuer,
            jwtAudience = jwtAudience,
            jwtSecret = jwtSecret
        )

        // Following routes
        followUser(followService)
        unfollowUser(followService)
    }
}
