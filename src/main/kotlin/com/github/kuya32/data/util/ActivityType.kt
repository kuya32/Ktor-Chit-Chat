package com.github.kuya32.data.util

sealed class ActivityType(type: Int) {
    object LikedPost: ActivityType(0)
    object CommentedOnPost: ActivityType(1)
    object FollowedUser: ActivityType(2)
}
