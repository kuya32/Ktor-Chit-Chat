package com.github.kuya32.data.repository.user

import com.github.kuya32.data.models.User
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.eq

class UserRepositoryImpl(
    db: CoroutineDatabase
) : UserRepository {

    private val users = db.getCollection<User>()

    override suspend fun createdUser(user: User) {
        users.insertOne(user)
    }

    override suspend fun getUserById(id: String): User? {
        return users.findOneById(id)
    }

    override suspend fun getUserByEmail(email: String): User? {
        return users.findOne(User::email eq email)
    }

}