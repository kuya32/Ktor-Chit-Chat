package com.github.kuya32.service

import com.github.kuya32.data.requests.FollowUpdateRequest
import com.github.kuya32.repository.follow.FollowRepository

class FollowService(
    private val followRepository: FollowRepository
) {

    suspend fun followUserIfExists(request: FollowUpdateRequest): Boolean {
        return followRepository.followUserIfExists(
            request.followingUserId,
            request.followedUserId
        )
    }

    suspend fun unfollowUserIfExists(request: FollowUpdateRequest): Boolean {
        return followRepository.unfollowUserIfExists(
            request.followingUserId,
            request.followedUserId
        )
    }
}