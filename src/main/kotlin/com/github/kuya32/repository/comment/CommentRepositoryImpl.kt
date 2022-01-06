package com.github.kuya32.repository.comment

import com.github.kuya32.data.models.Comment
import com.github.kuya32.data.models.Like
import com.github.kuya32.data.models.Post
import org.litote.kmongo.and
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.eq
import org.litote.kmongo.setValue

class CommentRepositoryImpl(
    db: CoroutineDatabase
): CommentRepository {

    private val comments = db.getCollection<Comment>()
    private val posts = db.getCollection<Post>()
    private val likes = db.getCollection<Like>()

    override suspend fun createComment(comment: Comment): String {
        comments.insertOne(comment)
        val oldCommentCount = posts.findOneById(comment.postId)?.commentCount ?: 0
        posts.updateOneById(comment.postId, setValue(Post::commentCount, oldCommentCount + 1))
        return comment.id
    }

    override suspend fun deleteComment(commentId: String): Boolean {
        val deleteCount = comments.deleteOneById(commentId).deletedCount
        return deleteCount > 0
    }

    override suspend fun deleteCommentsForPost(postId: String): Boolean {
        return comments.deleteMany(
            Comment::postId eq postId
        ).wasAcknowledged()
    }

    override suspend fun getCommentsForPost(postId: String, ownUserId: String): List<Comment> {
        return comments.find(Comment::postId eq postId).toList().map { comment ->
            val isLiked = likes.findOne(
                and(
                    Like::userId eq ownUserId,
                    Like::parentId eq comment.id
                )
            ) != null
            Comment(
                userId = comment.userId,
                postId = comment.postId,
                username = comment.username,
                profileImageUrl = comment.profileImageUrl,
                comment = comment.comment,
                likeCount = comment.likeCount,
                timestamp = comment.timestamp,
                id = comment.id
            )
        }
    }

    override suspend fun getComment(commentId: String): Comment? {
        return comments.findOneById(commentId)
    }
}