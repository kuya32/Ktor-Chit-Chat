package com.github.kuya32.routes

import com.github.kuya32.data.requests.CreatePostRequest
import com.github.kuya32.repository.post.PostRepository
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.createPostRoute(postRepository: PostRepository) {
    post("/api/post/create") {
        val request = call.receiveOrNull<CreatePostRequest>() ?: kotlin.run {
            call.respond(HttpStatusCode.BadRequest)
            return@post
        }
    }
}