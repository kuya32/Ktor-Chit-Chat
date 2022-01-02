package com.github.kuya32.data.util

sealed class ActivityType(type: Int) {
    object LikedPost: ActivityType(0)
    object LikeComment: ActivityType(1)
    object CommentedOnPost: ActivityType(2)
    object FollowedUser: ActivityType(3)
}
