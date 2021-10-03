package com.stegnerd.models

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(
    val email: String,
    val password: String
)

@Serializable
data class RegisterRequest(
    val trainerName: String,
    val email: String,
    val password: String
)