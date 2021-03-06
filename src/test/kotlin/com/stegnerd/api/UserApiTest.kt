package com.stegnerd.api

import com.stegnerd.api.user.UserApi
import com.stegnerd.api.user.UserApiImpl
import com.stegnerd.database.dao.UserDao
import com.stegnerd.statuspages.InvalidUserException
import com.stegnerd.stub.model.AuthStub.generateRegisterUserRequest
import com.stegnerd.stub.model.UserStub
import com.stegnerd.stub.model.UserStub.generateUser
import com.stegnerd.utils.PasswordWrapperContract
import io.mockk.clearMocks
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.assertThrows
import org.koin.dsl.module

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserApiTest : BaseApiTest() {

    private val userDao: UserDao = mockk()
    private val passwordWrapper: PasswordWrapperContract = mockk()
    private val api: UserApi = UserApiImpl

    init {
        startInjection(
            module {
                single { userDao }
                single { passwordWrapper }
            }
        )
    }

    @BeforeEach
    fun before() {
        clearMocks(userDao)
    }

    @Test
    fun `getUserByID returns user when user found`() {
        val user = generateUser()
        every { userDao.getUserByID(any()) } returns user

        val responseUser = api.getUserByID(8)
        assertThat(user).isEqualTo(responseUser)
    }

    @Test
    fun `getUserByID returns null when user not found`() {
        every { userDao.getUserByID(any()) } returns null

        val responseUser = api.getUserByID(8)
        assertThat(responseUser).isNull()
    }

    @Test
    fun `getUserByEmail returns user when user found`() {
        val user = generateUser()
        every { userDao.getUserByEmail(any()) } returns user

        val responseUser = api.getUserByEmail("name@sample.com")
        assertThat(user).isEqualTo(responseUser)
    }

    @Test
    fun `getUserByEmail returns null when user not found`() {
        every { userDao.getUserByEmail(any()) } returns null

        val responseUser = api.getUserByEmail("name@sample.com")
        assertThat(responseUser).isNull()
    }

    @Test
    fun `createAccount returns user when account created`() {
        val newUser = generateRegisterUserRequest()
        val user = generateUser(8)
        every { passwordWrapper.encryptPassword(any()) } returns "sample"
        every { userDao.insertUser(any()) } returns 8
        every { userDao.getUserByID(any()) } returns user

        val userCreated = api.createAccount(newUser)
        assertThat(userCreated).isTrue
    }

    @Test
    fun `createAccount throws error when failed to create Account`() {
        val newUser = generateRegisterUserRequest()
        val user = generateUser(8)
        every { passwordWrapper.encryptPassword(any()) } returns "sample"
        every { userDao.insertUser(any()) } throws InvalidUserException("Error while creating user.")
        every { userDao.getUserByID(any()) } returns user

        val exception = assertThrows<InvalidUserException> {
            api.createAccount(newUser)
        }
        assertThat("Error while creating user.").isEqualTo(exception.message)
    }

    @Test
    fun `updateAccount returns updated user when account updated successfully`() {
        val updateUserRequest = UserStub.generateUpdateUserRequest()
        val user = generateUser(8, updateUserRequest.name!!)

        every { userDao.updateUser(any(), any()) } returns user

        val updatedUser = api.updateAccount(user.id, updateUserRequest)
        assertThat(updatedUser?.name).isEqualTo(updateUserRequest.name)
    }

    @Test
    fun `deleteAccount returns true when account deleted`() {
        val id = 8
        every { userDao.deleteUser(any()) } returns true

        val deleteResult = api.deleteAccount(id)
        assertThat(deleteResult).isTrue
    }

    @Test
    fun `deleteAccount returns false when account deleted`() {
        val id = 8
        every { userDao.deleteUser(any()) } returns false

        val deleteResult = api.deleteAccount(id)
        assertThat(deleteResult).isFalse
    }
}