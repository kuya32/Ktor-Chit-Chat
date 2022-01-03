package com.github.kuya32.routes

import com.github.kuya32.data.models.Activity
import com.github.kuya32.data.requests.FollowUpdateRequest
import com.github.kuya32.data.responses.BasicApiResponse
import com.github.kuya32.data.util.ActivityType
import com.github.kuya32.data.util.ParentType
import com.github.kuya32.repository.follow.FollowRepository
import com.github.kuya32.service.ActivityService
import com.github.kuya32.service.FollowService
import com.github.kuya32.util.ApiResponseMessages.USER_NOT_FOUND
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.followUser(
    followService: FollowService,
    activityService: ActivityService
) {
    authenticate {
        post("/api/following/follow") {
            val request = call.receiveOrNull<FollowUpdateRequest>() ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@post
            }

            if (followService.followUserIfExists(request, call.userId)) {
                activityService.createActivity(
                    Activity(
                        timestamp = System.currentTimeMillis(),
                        parentId = "",
                        byUserId = call.userId,
                        toUserId = request.followedUserId,
                        type = ActivityType.FollowedUser.type,
                    )
                )
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
                        message = USER_NOT_FOUND
                    )
                )
            }
        }
    }
}

fun Route.unfollowUser(
    followService: FollowService
) {
    authenticate {
        delete("/api/following/unfollow") {
            val request = call.receiveOrNull<FollowUpdateRequest>() ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@delete
            }

            if (followService.unfollowUserIfExists(request, call.userId)) {
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
                        message = USER_NOT_FOUND
                    )
                )
            }
        }
    }
}