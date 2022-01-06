package com.github.kuya32.service

import com.github.kuya32.data.models.Activity
import com.github.kuya32.data.util.ActivityType
import com.github.kuya32.data.util.ParentType
import com.github.kuya32.repository.activity.ActivityRepository
import com.github.kuya32.repository.comment.CommentRepository
import com.github.kuya32.repository.post.PostRepository

class ActivityService(
    private val activityRepository: ActivityRepository,
    private val postRepository: PostRepository,
    private val commentRepository: CommentRepository
) {

    suspend fun getActivitiesForUser(
        userId: String,
        page: Int,
        pageSize: Int
    ): List<Activity> {
        return activityRepository.getActivitiesForUser(userId, page, pageSize)
    }

    suspend fun createActivity(activity: Activity) {
        activityRepository.createActivity(activity)
    }

    suspend fun addCommentActivity(
        byUserId: String,
        postId: String
    ): Boolean {
        val userIdOfPost = postRepository.getPost(postId)?.userId ?: return false
        if (byUserId == userIdOfPost) {
            return false
        }
        activityRepository.createActivity(
            Activity(
                timestamp = System.currentTimeMillis(),
                parentId = postId,
                byUserId = byUserId,
                toUserId = userIdOfPost,
                type = ActivityType.CommentedOnPost.type
            )
        )
        return true
    }

    suspend fun addLikeActivity(
        byUserId: String,
        parentId: String,
        parentType: ParentType
    ): Boolean {
        val toUserId = when (parentType) {
            is ParentType.Post -> {
                postRepository.getPost(parentId)?.userId
            }
            is ParentType.Comment -> {
                commentRepository.getComment(parentId)?.userId
            }
            is ParentType.None -> {
                return false
            }
        } ?: return false
        if (byUserId == toUserId) {
            return false
        }
        activityRepository.createActivity(
            Activity(
                timestamp = System.currentTimeMillis(),
                parentId = parentId,
                byUserId = byUserId,
                toUserId = toUserId,
                type = when(parentType) {
                    is ParentType.Post -> ActivityType.LikedPost.type
                    is ParentType.Comment -> ActivityType.LikeComment.type
                    else -> ActivityType.LikedPost.type
                }
            )
        )
        return true
    }

    suspend fun deleteActivity(activityId: String): Boolean {
        return activityRepository.deleteActivity(activityId)
    }
}