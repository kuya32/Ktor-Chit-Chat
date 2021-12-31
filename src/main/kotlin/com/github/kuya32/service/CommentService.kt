package com.github.kuya32.service

import com.github.kuya32.data.models.Comment
import com.github.kuya32.data.requests.CreateCommentRequest
import com.github.kuya32.repository.comment.CommentRepository
import com.github.kuya32.repository.user.UserRepository
import com.github.kuya32.util.Constants

class CommentService(
    private val commentRepository: CommentRepository,
    private val userRepository: UserRepository
) {

    suspend fun createComment(request: CreateCommentRequest, userId: String): ValidationEvent {
        request.apply {
            if (comment.isBlank() || postId.isBlank()) {
                return ValidationEvent.ErrorFieldEmpty
            }
            if (comment.length > Constants.MAX_COMMENT_LENGTH) {
                return ValidationEvent.ErrorCommentTooLong
            }
        }
        val user = userRepository.getUserById(userId) ?: return ValidationEvent.UserNotFound
        commentRepository.createComment(
            Comment(
                userId = userId,
                postId = request.postId,
                username = user.username,
                profileImageUrl = user.profileImageUrl,
                comment = request.comment,
                likeCount = 0,
                timestamp = System.currentTimeMillis()
            )
        )
        return ValidationEvent.Success
    }

    suspend fun deleteComment(commentId: String): Boolean {
        return commentRepository.deleteComment(commentId)
    }

    suspend fun deleteCommentsForPost(postId: String) {
        commentRepository.deleteCommentsForPost(postId)
    }

    suspend fun getCommentsForPost(postId: String, ownUserId: String): List<Comment> {
        return commentRepository.getCommentsForPost(postId, ownUserId)
    }

    suspend fun getComment(commentId: String): Comment? {
        return commentRepository.getComment(commentId)
    }

    sealed class ValidationEvent {
        object ErrorFieldEmpty: ValidationEvent()
        object ErrorCommentTooLong: ValidationEvent()
        object UserNotFound: ValidationEvent()
        object Success: ValidationEvent()
    }
}