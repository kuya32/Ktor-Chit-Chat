package com.github.kuya32.repository.post

import com.github.kuya32.data.models.Post
import com.github.kuya32.util.Constants

interface PostRepository {

    suspend fun createPost(post: Post): Boolean

    suspend fun deletePost(postId: String)

    suspend fun getPostsByFollow(
        ownUserId: String,
        page: Int,
        pageSize: Int = Constants.DEFAULT_PAGE_SIZE
    ): List<Post>
}