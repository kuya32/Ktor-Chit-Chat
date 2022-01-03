package com.github.kuya32.service

import com.github.kuya32.data.models.User
import com.github.kuya32.data.requests.CreateAccountRequest
import com.github.kuya32.data.responses.BasicApiResponse
import com.github.kuya32.data.responses.UserResponseItem
import com.github.kuya32.repository.follow.FollowRepository
import com.github.kuya32.repository.user.UserRepository
import com.github.kuya32.util.ApiResponseMessages
import io.ktor.application.*
import io.ktor.response.*

class UserService(
    private val userRepository: UserRepository,
    private val followRepository: FollowRepository
) {

    suspend fun doesUserWithEmailExist(email: String): Boolean {
        return userRepository.getUserByEmail(email) != null
    }

    suspend fun getUserByEmail(email: String): User? {
        return userRepository.getUserByEmail(email)
    }

    fun isValidPassword(enteredPassword: String, actualPassword: String): Boolean {
        return enteredPassword == actualPassword
    }

    suspend fun searchForUsers(query: String, userId: String): List<UserResponseItem> {
        val users = userRepository.searchForUsers(query)
        val followsByUser = followRepository.getFollowsByUser(userId)
        return users.map { user ->
            val isFollowing = followsByUser.find { it.followedUserId == user.id } != null
            UserResponseItem(
                username = user.username,
                profilePictureUrl = user.profileImageUrl,
                bio = user.bio,
                isFollowing = isFollowing
            )
        }
    }

    suspend fun createUser(request: CreateAccountRequest) {
        userRepository.createdUser(
            User(
                email = request.email,
                username = request.username,
                password = request.password,
                profileImageUrl = "",
                bio = "",
                githubUrl = null,
                instagramUrl = null,
                linkedInUrl = null
            )
        )
    }

    fun validateCreatedAccountRequest(request: CreateAccountRequest): ValidateEvent {
        if (request.email.isBlank() || request.password.isBlank() || request.username.isBlank
                ()) {
            return ValidateEvent.ErrorFieldEmpty
        }
        return ValidateEvent.Success
    }

    sealed class ValidateEvent {
        object ErrorFieldEmpty: ValidateEvent()
        object Success: ValidateEvent()
    }
}