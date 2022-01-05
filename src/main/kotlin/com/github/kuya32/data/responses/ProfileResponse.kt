package com.github.kuya32.data.responses

data class ProfileResponse(
    val userId: String,
    val username: String,
    val bio: String,
    val followerCount: Int,
    val followingCount: Int,
    val postCount: Int,
    val profilePictureUrl: String,
    val bannerUrl: String?,
    val topSkills: List<String>,
    val githubUrl: String?,
    val instagramUrl: String?,
    val linkedInUrl: String?,
    val isOwnProfile: Boolean,
    val isFollowing: Boolean
)
