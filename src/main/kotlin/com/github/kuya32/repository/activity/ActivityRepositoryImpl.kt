package com.github.kuya32.repository.activity

import com.github.kuya32.data.models.Activity
import com.github.kuya32.data.models.User
import org.litote.kmongo.`in`
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.eq

class ActivityRepositoryImpl(
    db: CoroutineDatabase
) : ActivityRepository {

    private val activities = db.getCollection<Activity>()
    private val users = db.getCollection<User>()

    override suspend fun getActivitiesForUser(
        userId: String,
        page: Int,
        pageSize: Int
    ): List<Activity> {
        val activities = activities.find(Activity::toUserId eq userId)
            .skip(page * pageSize)
            .limit(pageSize)
            .descendingSort(Activity::timestamp)
            .toList()
        val userIds = activities.map { it.byUserId }
        val users = users.find(User::id `in` userIds).toList()
        // TODO: Change activity type
        return activities.mapIndexed { i, activity ->
            Activity(
                timestamp = activity.timestamp,
                parentId = activity.parentId,
                byUserId = "",
                toUserId = "",
                type = activity.type,
                id = activity.id
            )
        }
    }

    override suspend fun createActivity(activity: Activity) {
        activities.insertOne(activity)
    }

    override suspend fun deleteActivity(activityId: String): Boolean {
        return activities.deleteOne(activityId).wasAcknowledged()
    }
}