package com.stegnerd.models

import io.ktor.auth.Principal
import kotlinx.serialization.Serializable

@Serializable
data class RegisterUserRequest(
    val name: String,
    val trainerName: String,
    val email: String,
    val password: String
) : Principal

@Serializable
data class LoginUserRequest(val email: String, val password: String)

@Serializable
data class LoginTokenResponse(val credentials: CredentialsResponse)

@Serializable
data class CredentialsResponse(val accessToken: String, val refreshToken: String)

@Serializable
data class RefreshTokenRequest(val username: String, val refreshToken: String)