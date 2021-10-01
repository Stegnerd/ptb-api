package com.stegnerd.models

data class LoginRequest(
    val email: String,
    val password: String
)

data class RegisterRequest(
    val trainerName: String,
    val email: String,
    val password: String
)