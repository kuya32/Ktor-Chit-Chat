package com.github.kuya32.service

import com.github.kuya32.data.models.Post
import com.github.kuya32.data.requests.CreatePostRequest
import com.github.kuya32.repository.post.PostRepository
import com.github.kuya32.util.Constants

class PostService(
    private val postRepository: PostRepository
) {

    suspend fun createPost(request: CreatePostRequest, userId: String, imageUrl: String): Boolean {
        return postRepository.createPost(
            Post(
                imageUrl = imageUrl,
                userId = userId,
                timestamp = System.currentTimeMillis(),
                description = request.description
            )
        )
    }

    suspend fun getPostsForFollow(
        ownUserId: String,
        page: Int = 0,
        pageSize: Int = Constants.DEFAULT_PAGE_SIZE
    ): List<Post> {
        return postRepository.getPostsByFollow(ownUserId, page, pageSize)
    }

    suspend fun getPost(postId: String): Post? = postRepository.getPost(postId)

    suspend fun deletePost(postId: String) {
        postRepository.deletePost(postId)
    }
}