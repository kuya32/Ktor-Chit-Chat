package com.github.kuya32.service

import com.github.kuya32.repository.likes.LikeRepository

class LikeService(
    private val likeRepository: LikeRepository
) {
    suspend fun likeParent(userId: String, parentId: String, parentType: Int): Boolean {
        return likeRepository.likeParent(userId, parentId, parentType)
    }

    suspend fun unlikeParent(userId: String, parentId: String, parentType: Int): Boolean {
        return likeRepository.unlikeParent(userId, parentId, parentType)
    }

    suspend fun deleteLikesForParent(parentId: String) {
        likeRepository.deleteLikesForParent(parentId)
    }
}