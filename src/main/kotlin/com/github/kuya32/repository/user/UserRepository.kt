package com.github.kuya32.repository.user

import com.github.kuya32.data.models.User
import com.github.kuya32.data.requests.UpdateProfileRequest

interface UserRepository {

    suspend fun createdUser(user: User)

    suspend fun getUserById(id: String): User?

    suspend fun getUserByEmail(email: String): User?

    suspend fun updateUser(userId: String, profileImageUrl: String?, bannerUrl: String?,
                           updateProfileRequest: UpdateProfileRequest): Boolean

    suspend fun doesPasswordForUserMatch(email: String, enteredPassword: String): Boolean

    suspend fun doesEmailBelongToUserId(email: String, userId: String): Boolean

    suspend fun searchForUsers(query: String): List<User>

    suspend fun getUsers(userIds: List<String>): List<User>
}