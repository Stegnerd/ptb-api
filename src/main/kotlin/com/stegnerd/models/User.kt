package com.stegnerd.models

import io.ktor.server.auth.Principal
import kotlinx.serialization.Serializable
import java.time.LocalDateTime


//region models

data class User(
    val id: Int,
    val name: String,
    val email: String,
    val password: String,
    val active: Boolean,
    val created: LocalDateTime,
    var updated: LocalDateTime
) : Principal

//endregion models

//region requests

@Serializable
data class UpdateUserRequest(
    val name: String? = null
) : Principal

//endregion requests

//region responses

@Serializable
data class ResponseUser(
    val id: Int,
    val name: String,
    val email: String
)

//endregion responses

//region Extensions

fun User.toResponseUser() = ResponseUser(
    this.id,
    this.name,
    this.email
)

//endregion Extensions