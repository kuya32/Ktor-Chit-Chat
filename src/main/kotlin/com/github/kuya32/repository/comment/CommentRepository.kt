package com.github.kuya32.repository.comment

import com.github.kuya32.data.models.Comment

interface CommentRepository {

    suspend fun createComment(comment: Comment): String

    suspend fun deleteComment(commentId: String): Boolean

    suspend fun deleteCommentsForPost(postId: String): Boolean

    suspend fun getCommentsForPost(postId: String, ownUserId: String): List<Comment>

    suspend fun getComment(commentId: String): Comment?
}