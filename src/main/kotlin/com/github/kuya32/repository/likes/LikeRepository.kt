package com.github.kuya32.repository.likes

interface LikeRepository {

    suspend fun likeParent(userId: String, parentId: String, parentType: Int): Boolean

    suspend fun unlikeParent(userId: String, parentId: String, parentType: Int): Boolean


}