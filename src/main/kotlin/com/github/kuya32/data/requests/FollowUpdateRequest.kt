package com.github.kuya32.data.requests

data class FollowUpdateRequest(
    val followingUserId: String,
    val followedUserId: String,
)
