package com.github.kuya32.data.models

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

data class Post(
    @BsonId
    val id: String = ObjectId().toString(),
    val userId: String,
    val timestamp: Long,
    val imageUrl: String,
    val description: String
)
