package com.github.kuya32.di

import com.github.kuya32.repository.comment.CommentRepository
import com.github.kuya32.repository.comment.CommentRepositoryImpl
import com.github.kuya32.repository.follow.FollowRepository
import com.github.kuya32.repository.follow.FollowRepositoryImpl
import com.github.kuya32.repository.likes.LikeRepository
import com.github.kuya32.repository.likes.LikeRepositoryImpl
import com.github.kuya32.repository.post.PostRepository
import com.github.kuya32.repository.post.PostRepositoryImpl
import com.github.kuya32.repository.user.UserRepository
import com.github.kuya32.repository.user.UserRepositoryImpl
import com.github.kuya32.service.*
import com.github.kuya32.util.Constants
import org.koin.dsl.module
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo

val mainModule = module {
    single {
        val client = KMongo.createClient().coroutine
        client.getDatabase(Constants.DATABASE_NAME)
    }
    single<UserRepository> {
        UserRepositoryImpl(get())
    }
    single<FollowRepository> {
        FollowRepositoryImpl(get())
    }
    single<PostRepository> {
        PostRepositoryImpl(get())
    }
    single<LikeRepository> {
        LikeRepositoryImpl(get())
    }
    single<CommentRepository> {
        CommentRepositoryImpl(get())
    }

    single { UserService(get()) }
    single { FollowService(get()) }
    single { PostService(get()) }
    single { LikeService(get()) }
    single { CommentService(get(), get()) }
}