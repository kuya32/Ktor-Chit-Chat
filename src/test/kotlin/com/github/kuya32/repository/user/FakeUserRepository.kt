package com.github.kuya32.repository.user

import com.github.kuya32.data.models.User

class FakeUserRepository : UserRepository {

    val users = mutableListOf<User>()

    override suspend fun createdUser(user: User) {
        users.add(user)
    }

    override suspend fun getUserById(id: String): User? {
        return users.find { it.id == id }
    }

    override suspend fun getUserByEmail(email: String): User? {
        return users.find { it.email == email }
    }
}