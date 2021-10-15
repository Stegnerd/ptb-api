package com.stegnerd.modules.registration

import com.stegnerd.api.user.UserApi
import com.stegnerd.models.LoginTokenResponse
import com.stegnerd.models.LoginUserRequest
import com.stegnerd.models.RefreshTokenRequest
import com.stegnerd.models.RegisterUserRequest
import com.stegnerd.models.ResponseUser
import com.stegnerd.models.toResponseUser
import com.stegnerd.modules.BaseController
import com.stegnerd.modules.auth.TokenProvider
import com.stegnerd.utils.AuthenticationException
import com.stegnerd.utils.InvalidUserException
import com.stegnerd.utils.PasswordWrapperContract
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

interface RegistrationController {
    suspend fun login(loginRequest: LoginUserRequest): LoginTokenResponse
    suspend fun register(registerRequest: RegisterUserRequest): ResponseUser
    suspend fun refreshToken(refreshTokenRequest: RefreshTokenRequest): LoginTokenResponse
}

class RegistrationControllerImpl: BaseController(), RegistrationController, KoinComponent {

    private val userApi by inject<UserApi>()
    private val passwordWrapper by inject<PasswordWrapperContract>()
    private val tokenProvider by inject<TokenProvider>()

    override suspend fun login(loginRequest: LoginUserRequest): LoginTokenResponse  = dbQuery {
        userApi.getUserByEmail(loginRequest.email)?.let { user ->
            if(passwordWrapper.validatePassword(loginRequest.password, user.password)){
                val credentialsResponse = tokenProvider.createTokens(user)
                LoginTokenResponse(credentialsResponse)
            }else {
                throw AuthenticationException("Invalid Credentials.")
            }
        } ?: throw AuthenticationException("Invalid Credentials.")
    }

    override suspend fun register(registerRequest: RegisterUserRequest): ResponseUser {
        val user = dbQuery {
            userApi.getUserByEmail(registerRequest.email)?.let {
                throw InvalidUserException("Information already taken.")
            }
            userApi.createAccount(registerRequest) ?: throw UnknownError("Internal server error.")
        }
        return user.toResponseUser()
    }

    override suspend fun refreshToken(refreshTokenRequest: RefreshTokenRequest): LoginTokenResponse = dbQuery {
        tokenProvider.verifyToken(refreshTokenRequest.refreshToken)?.let {
            userApi.getUserByID(it)?.let {
                val credentialsResponse = tokenProvider.createTokens(it)
                LoginTokenResponse(credentialsResponse)
            } ?: throw AuthenticationException("Invalid Credentials.")
        } ?: throw AuthenticationException("Invalid Credentials.")
    }
}