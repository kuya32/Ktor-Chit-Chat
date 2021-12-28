package com.github.kuya32.routes

import com.github.kuya32.data.requests.CreatePostRequest
import com.github.kuya32.plugins.userId
import com.github.kuya32.repository.post.PostRepository
import com.github.kuya32.service.PostService
import com.github.kuya32.util.Constants
import com.github.kuya32.util.QueryParams
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.auth.jwt.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.createPostRoute(
    postService: PostService
) {
    authenticate {
        post("/api/post/create") {
            val request = call.receiveOrNull<CreatePostRequest>() ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@post
            }
        }
    }
}

fun Route.getPostsForFollows(
    postService: PostService
) {
    authenticate {
        get("/api/post/get") {
            val page = call.parameters[QueryParams.PARAM_PAGE]?.toIntOrNull() ?: 0
            val pageSize = call.parameters[QueryParams.PARAM_PAGE_SIZE]?.toIntOrNull() ?:
            Constants.DEFAULT_PAGE_SIZE

            val posts = postService.getPostsForFollow(
                call.userId,
                page,
                pageSize
            )
            call.respond(
                HttpStatusCode.OK,
                posts
            )
        }
    }
}