package com.github.kuya32.routes

import com.github.kuya32.data.requests.CreateActivityRequest
import com.github.kuya32.data.requests.DeleteActivityRequest
import com.github.kuya32.service.ActivityService
import com.github.kuya32.util.Constants
import com.github.kuya32.util.QueryParams
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import org.koin.ktor.ext.get


fun Route.getActivitiesForUser(
    activityService: ActivityService
) {
    authenticate {
        get("/api/activity/get") {
            val page = call.parameters[QueryParams.PARAM_PAGE]?.toIntOrNull() ?: 0
            val pageSize = call.parameters[QueryParams.PARAM_PAGE_SIZE]?.toIntOrNull() ?: Constants.DEFAULT_ACTIVITY_PAGE_SIZE

            val activities = activityService.getActivitiesForUser(
                call.userId,
                page,
                pageSize
            )
            call.respond(
                HttpStatusCode.OK,
                activities
            )
        }
    }
}