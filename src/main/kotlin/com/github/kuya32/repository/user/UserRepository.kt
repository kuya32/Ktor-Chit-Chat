package com.github.kuya32.repository.user

import com.github.kuya32.data.models.User

interface UserRepository {

    suspend fun createdUser(user: User)

    suspend fun getUserById(id: String): User?

    suspend fun getUserByEmail(email: String): User?

    suspend fun doesPasswordForUserMatch(email: String, enteredPassword: String): Boolean
}