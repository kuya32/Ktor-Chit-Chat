package com.github.kuya32.data.models

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

data class Comment(
    val userId: String,
    val postId: String,
    val username: String,
    val profileImageUrl: String,
    val comment: String,
    val likeCount: Int,
    val timestamp: Long,
    @BsonId
    val id: String = ObjectId().toString()
)
