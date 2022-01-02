package com.github.kuya32.data.models

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

data class Activity(
    val timestamp: Long,
    val parentId: String,
    val byUserId: String,
    val toUserId: String,
    val type: Int,
    @BsonId
    val id: String = ObjectId().toString(),
)
