package com.stegnerd.stub.model

import com.stegnerd.models.ResponseUser
import com.stegnerd.models.UpdateUserRequest
import com.stegnerd.models.User
import java.time.LocalDateTime

object UserStub {

    fun generateUser(ID: Int = 8, name: String = "name"): User {
        return User(
            ID,
            name,
            email = "name@example.com",
            password = "sample",
            active = true,
            created = LocalDateTime.now(),
            updated = LocalDateTime.now()
        )
    }

    fun generateUpdateUserRequest(): UpdateUserRequest {
        return UpdateUserRequest(
            name = "name updated"
        )
    }

    fun generateResponseUser(): ResponseUser {
        return ResponseUser(
            id = 8,
            name = "name",
            email = "name@example.com"
        )
    }
}