package com.github.kuya32.data.models

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

data class Comment(
    @BsonId
    val id: String = ObjectId().toString(),
    val timestamp: Long,
    val userId: String,
    val postId: String,
    val comment: String
)
