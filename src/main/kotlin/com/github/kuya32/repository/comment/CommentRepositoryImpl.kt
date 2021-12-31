package com.github.kuya32.repository.comment

import com.github.kuya32.data.models.Comment
import com.github.kuya32.data.models.Post
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.eq
import org.litote.kmongo.setValue

class CommentRepositoryImpl(
    db: CoroutineDatabase
): CommentRepository {

    private val comments = db.getCollection<Comment>()
    private val posts = db.getCollection<Post>()

    override suspend fun createComment(comment: Comment) {
        comments.insertOne(comment)
        val oldCommentCount = posts.findOneById(comment.postId)?.commentCount ?: 0
        posts.updateOneById(comment.postId, setValue(Post::commentCount, oldCommentCount + 1))
    }

    override suspend fun deleteComment(commentId: String): Boolean {
        val deleteCount = comments.deleteOneById(commentId).deletedCount
        return deleteCount > 0
    }

    override suspend fun getCommentsForPost(postId: String): List<Comment> {
        return comments.find(Comment::postId eq postId).toList()
    }

    override suspend fun getComment(commentId: String): Comment? {
        return comments.findOneById(commentId)
    }
}