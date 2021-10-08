package com.stegnerd.utils

// Auth
data class AuthenticationException(override val message: String = "Authentication failed") : Exception()
data class AuthorizationException(override val message: String = "You are not authorized to use this service") : Exception()


// User
data class InvalidUserException(override val message: String = "Invalid User" ) : Exception()