package com.stegnerd.controllers

import com.stegnerd.api.user.UserApi
import com.stegnerd.modules.user.UserController
import com.stegnerd.modules.user.UserControllerImpl
import com.stegnerd.stub.model.UserStub.generateUpdateUserRequest
import com.stegnerd.stub.model.UserStub.generateUser
import io.mockk.clearMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.koin.dsl.module

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserControllerTest : BaseControllerTest() {

    private val userApi: UserApi = mockk()
    private val controller: UserController by lazy { UserControllerImpl() }
    val userId = 8

    init {
        startInjection(
            module {
                single { userApi }
            }
        )
    }

    @BeforeEach
    override fun before() {
        super.before()
        clearMocks(userApi)
    }

    @Test
    fun `updateAccount returns updated user when successful`() {
        val payload = generateUpdateUserRequest()
        val user = generateUser(userId)
        val updatedUser = user.copy(name = payload.name!!, trainerName = payload.trainerName!!)

        every { userApi.updateAccount(any(), any()) } returns updatedUser

        runBlocking {
            val responseUser = controller.updateAccount(userId, payload)

            assertThat(responseUser.id).isEqualTo(userId)
            assertThat(responseUser.name).isEqualTo(payload.name)
            assertThat(responseUser.trainerName).isEqualTo(payload.trainerName)
        }
    }

    @Test
    fun `updateAccount throws error when api fails`() {
        val payload = generateUpdateUserRequest()

        every { userApi.updateAccount(any(), any()) } returns null

        assertThrows(UnknownError::class.java) {
            runBlocking {
                controller.updateAccount(userId, payload)
            }
        }
    }

    @Test
    fun `deleteAccount returns true when account deleted` () {

        every { userApi.deleteAccount(any()) } returns true

        runBlocking {
            controller.deleteAccount(userId)

            verify { userApi.deleteAccount(any()) }
        }
    }

    @Test
    fun `deleteAccount throws error when api fails`() {

        every { userApi.deleteAccount(any()) } throws Exception()

        assertThrows(UnknownError::class.java) {
            runBlocking {
                controller.deleteAccount(userId)
            }
        }
    }
}