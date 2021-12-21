package com.github.kuya32.routes

import com.github.kuya32.data.models.User
import com.github.kuya32.data.requests.CreateAccountRequest
import com.github.kuya32.data.responses.BasicApiResponse
import com.github.kuya32.di.testModule
import com.github.kuya32.plugins.configureSerialization
import com.github.kuya32.repository.user.FakeUserRepository
import com.github.kuya32.util.ApiResponseMessages
import com.google.common.truth.Truth.assertThat
import com.google.gson.Gson
import io.ktor.application.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.routing.*
import io.ktor.server.testing.*
import kotlinx.coroutines.runBlocking
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.test.KoinTest
import org.koin.test.inject
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test

internal class AuthRouteKtTest : KoinTest {

    private val userRepository by inject<FakeUserRepository>()

    private val gson = Gson()

    @BeforeTest
    fun setUp() {
        startKoin {
            modules(testModule)
        }
    }

    @AfterTest
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun `Create user, no body attached, response with BadRequest`() {
        withTestApplication(
            moduleFunction = {
                install(Routing) {
                    createUser(userRepository)
                }
            }
        ) {
            val request = handleRequest (
                method = HttpMethod.Post,
                uri = "/api/user/create"
            )

            assertThat(request.response.status()).isEqualTo(HttpStatusCode.BadRequest)
        }
    }

    @Test
    fun `Create user, user already exists, response with unsuccessful`() = runBlocking {
        val user = User(
            email = "test@test.com",
            username = "test",
            password = "test",
            profileImageUrl = "",
            bio = "",
            githubUrl = null,
            instagramUrl = null,
            linkedInUrl = null
        )
        userRepository.createdUser(user)
        withTestApplication(
            moduleFunction = {
                configureSerialization()
                install(Routing) {
                    createUser(userRepository)
                }
            }
        ) {
            val request = handleRequest (
                method = HttpMethod.Post,
                uri = "/api/user/create"
            ) {
                addHeader("Content-Type", "application/json")
                val request = CreateAccountRequest(
                    email = "test@test.com",
                    username = "asdf",
                    password = "asdf"
                )
                setBody(gson.toJson(request))
            }

            val response = gson.fromJson(
                request.response.content ?: "",
                BasicApiResponse::class.java
            )

            assertThat(response.successful).isFalse()
            assertThat(response.message).isEqualTo(ApiResponseMessages.USER_ALREADY_EXISTS)
        }
    }

    @Test
    fun `Create user, email is empty, response with unsuccessful`() = runBlocking {
        withTestApplication(
            moduleFunction = {
                configureSerialization()
                install(Routing) {
                    createUser(userRepository)
                }
            }
        ) {
            val request = handleRequest (
                method = HttpMethod.Post,
                uri = "/api/user/create"
            ) {
                addHeader("Content-Type", "application/json")
                val request = CreateAccountRequest(
                    email = "",
                    username = "",
                    password = ""
                )
                setBody(gson.toJson(request))
            }

            val response = gson.fromJson(
                request.response.content ?: "",
                BasicApiResponse::class.java
            )

            assertThat(response.successful).isFalse()
            assertThat(response.message).isEqualTo(ApiResponseMessages.FIELDS_BLANK)
        }
    }

    @Test
    fun `Create user, valid data, response with successful`() {
        withTestApplication(
            moduleFunction = {
                configureSerialization()
                install(Routing) {
                    createUser(userRepository)
                }
            }
        ) {
            val request = handleRequest (
                method = HttpMethod.Post,
                uri = "/api/user/create"
            ) {
                addHeader("Content-Type", "application/json")
                val request = CreateAccountRequest(
                    email = "test@test.com",
                    username = "test",
                    password = "test"
                )
                setBody(gson.toJson(request))
            }

            val response = gson.fromJson(
                request.response.content ?: "",
                BasicApiResponse::class.java
            )

            assertThat(response.successful).isTrue()
            runBlocking {
                val isUserInDb = userRepository.getUserByEmail("test@test.com") != null
                assertThat(isUserInDb).isTrue()
            }
        }
    }


}