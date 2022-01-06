package com.github.kuya32.repository.post

import com.github.kuya32.data.models.Following
import com.github.kuya32.data.models.Like
import com.github.kuya32.data.models.Post
import com.github.kuya32.data.models.User
import com.github.kuya32.data.responses.PostResponse
import org.litote.kmongo.`in`
import org.litote.kmongo.and
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.eq

class PostRepositoryImpl(
    db: CoroutineDatabase
) : PostRepository {

    private val posts = db.getCollection<Post>()
    private val following = db.getCollection<Following>()
    private val users = db.getCollection<User>()
    private val likes = db.getCollection<Like>()

    override suspend fun createPost(post: Post): Boolean {
        return posts.insertOne(post).wasAcknowledged()
    }

    override suspend fun deletePost(postId: String) {
        posts.deleteOneById(postId)
    }

    override suspend fun getPostsByFollow(ownUserId: String, page: Int, pageSize: Int): List<Post> {
        TODO("Not yet implemented")
    }


//    override suspend fun getPostsByFollow(
//        ownUserId: String,
//        page: Int,
//        pageSize: Int
//    ): List<Post> {
//        val userIdsFromFollows = following.find(Following::followingUserId eq ownUserId)
//            .toList()
//            .map {
//                it.followedUserId
//            }
//        return posts.find()
//    }

    override suspend fun getPostsForProfile(
        ownUserId: String,
        userId: String,
        page: Int,
        pageSize: Int
    ): List<PostResponse> {
        val user = users.findOneById(userId) ?: return emptyList()
        return posts.find(Post::userId eq userId)
            .skip(page * pageSize)
            .limit(pageSize)
            .descendingSort(Post::timestamp)
            .toList()
            .map { post ->
                val isLiked = likes.findOne(
                    and(
                        Like::parentId eq post.id,
                        Like::userId eq ownUserId
                    )
                ) != null
                PostResponse(
                    id = post.id,
                    userId = userId,
                    username = user.username,
                    imageUrl = post.imageUrl,
                    profilePictureUrl = user.profileImageUrl,
                    description = post.description,
                    likeCount = post.likeCount,
                    commentCount = post.commentCount,
                    isLiked = isLiked,
                    isOwnPost = ownUserId == post.userId
                )
            }
    }

    override suspend fun getPost(postId: String): Post? {
        return posts.findOneById(postId)
    }
}