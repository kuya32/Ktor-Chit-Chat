package com.github.kuya32.data.requests

data class CreateCommentRequest(
    val comment: String,
    val postId: String
)
