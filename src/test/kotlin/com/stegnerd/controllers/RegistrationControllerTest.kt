package com.stegnerd.controllers

import com.stegnerd.api.user.UserApi
import com.stegnerd.modules.auth.TokenProvider
import com.stegnerd.modules.registration.RegistrationController
import com.stegnerd.modules.registration.RegistrationControllerImpl
import com.stegnerd.stub.model.AuthStub.generateCredentialResponse
import com.stegnerd.stub.model.AuthStub.generateLoginUserRequest
import com.stegnerd.stub.model.AuthStub.generateRegisterUserRequest
import com.stegnerd.stub.model.UserStub.generateUser
import com.stegnerd.utils.AuthenticationException
import com.stegnerd.utils.InvalidUserException
import com.stegnerd.utils.PasswordWrapperContract
import io.mockk.clearMocks
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.koin.dsl.module

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RegistrationControllerTest : BaseControllerTest() {

    private val userApi: UserApi = mockk()
    private val passwordWrapper: PasswordWrapperContract = mockk()
    private val tokenProvider: TokenProvider = mockk()
    private val controller: RegistrationController by lazy { RegistrationControllerImpl() }
    private val userID = 8

    init {
        startInjection(
            module {
                single { userApi }
                single { passwordWrapper }
                single { tokenProvider }
            }
        )
    }

    @BeforeEach
    override fun before() {
        super.before()
        clearMocks(userApi)
    }

    @Test
    fun `register creates valid user`() {
        val registerUserRequest = generateRegisterUserRequest()
        val createdUser = generateUser(userID)

        every { userApi.getUserByEmail(any()) } returns null
        every { userApi.createAccount(any()) } returns createdUser

        runBlocking {
            val responseUser = controller.register(registerUserRequest)

            assertThat(responseUser.id).isEqualTo(userID)
            assertThat(responseUser.email).isEqualTo(registerUserRequest.email)
            assertThat(responseUser.name).isEqualTo(registerUserRequest.name)
            assertThat(responseUser.trainerName).isEqualTo(registerUserRequest.trainerName)
        }
    }

    @Test
    fun `register returns error when email already taken`() {
        val registerUserRequest = generateRegisterUserRequest()
        val createdUser = generateUser(userID)

        every { userApi.getUserByEmail(any()) } returns createdUser

        assertThrows(InvalidUserException::class.java){
            runBlocking { controller.register(registerUserRequest) }
        }
    }

    @Test
    fun `register returns exception when api return error`() {
        val registerUserRequest = generateRegisterUserRequest()

        every { userApi.getUserByEmail(any()) } returns null
        every { userApi.createAccount(registerUserRequest) } returns null

        assertThrows(UnknownError::class.java) {
            runBlocking { controller.register(registerUserRequest) }
        }
    }

    @Test
    fun `login returns token when valid credentials`() {
        val loginUserRequest = generateLoginUserRequest()

        every { userApi.getUserByEmail(any()) } returns generateUser()
        every { passwordWrapper.validatePassword(any(), any()) } returns true
        every { tokenProvider.createTokens(any()) } returns generateCredentialResponse()

        runBlocking {
            val loginTokenResponse = controller.login(loginUserRequest)

            assertThat(loginTokenResponse.credentials.accessToken).isNotNull
            assertThat(loginTokenResponse.credentials.refreshToken).isNotNull
        }
    }

    @Test
    fun `login returns exception when wrong credentials`() {
        val loginUserRequest = generateLoginUserRequest()

        every { userApi.getUserByEmail(any()) } returns generateUser()
        every { passwordWrapper.validatePassword(any(), any()) } returns false

        assertThrows(AuthenticationException::class.java) {
            runBlocking {
                controller.login(loginUserRequest)
            }
        }
    }

    @Test
    fun `login returns exception when api returns no user`(){
        val loginUserRequest = generateLoginUserRequest()

        every { userApi.getUserByEmail(any()) } returns null

        assertThrows(AuthenticationException::class.java) {
            runBlocking {
                controller.login(loginUserRequest)
            }
        }
    }
}