package com.stegnerd.stub.model

import com.stegnerd.models.CredentialsResponse
import com.stegnerd.models.LoginUserRequest
import com.stegnerd.models.RefreshTokenRequest
import com.stegnerd.models.RegisterUserRequest

object AuthStub {
    fun generateRegisterUserRequest(): RegisterUserRequest {
        return RegisterUserRequest(
            name = "name",
            trainerName = "trainer_name",
            email = "name@example.com",
            password = "sample"
        )
    }

    fun generateLoginUserRequest(): LoginUserRequest {
        return LoginUserRequest(
            email = "name@example.com",
            password = "sample"
        )
    }

    fun generateCredentialResponse(): CredentialsResponse {
        return CredentialsResponse(
            accessToken = "token",
            refreshToken = "refresh"
        )
    }

    fun generateRefreshTokenRequest(): RefreshTokenRequest {
        return RefreshTokenRequest(
            username = "username",
            refreshToken = "refresh"
        )
    }
}