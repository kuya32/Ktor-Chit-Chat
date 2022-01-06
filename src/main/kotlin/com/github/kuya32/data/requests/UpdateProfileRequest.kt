package com.github.kuya32.data.requests

data class UpdateProfileRequest(
    val username: String,
    val bio: String,
    val instagramUrl: String,
    val linkedInUrl: String,
    val githubUrl: String,
    val skills: List<String>
)
