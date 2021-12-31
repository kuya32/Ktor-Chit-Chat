package com.github.kuya32.repository.post

import com.github.kuya32.data.models.Following
import com.github.kuya32.data.models.Post
import org.litote.kmongo.`in`
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.eq

class PostRepositoryImpl(
    db: CoroutineDatabase
) : PostRepository {

    private val posts = db.getCollection<Post>()
    private val following = db.getCollection<Following>()

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

    override suspend fun getPost(postId: String): Post? {
        return posts.findOneById(postId)
    }
}