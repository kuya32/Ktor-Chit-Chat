package com.github.kuya32.data.repository.user

import com.github.kuya32.data.models.User

interface UserRepository {

    suspend fun createdUser(user: User)

    suspend fun getUserById(id: String): User?

    suspend fun getUserByEmail(email: String): User?


}