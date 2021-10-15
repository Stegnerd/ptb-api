package com.stegnerd.stub.model

import com.stegnerd.models.ResponseUser
import com.stegnerd.models.UpdateUserRequest
import com.stegnerd.models.User
import java.time.LocalDateTime

object UserStub {

    fun generateUser(ID: Int = 8, name: String = "name", trainerName: String = "trainer_name"): User {
        return User(
            ID,
            name,
            trainerName,
            email = "name@example.com",
            password = "sample",
            active = true,
            created = LocalDateTime.now(),
            updated = LocalDateTime.now()
        )
    }

    fun generateUpdateUserRequest(): UpdateUserRequest {
        return UpdateUserRequest(
            name = "name updated",
            trainerName = "updated trainer name"
        )
    }

    fun generateResponseUser(): ResponseUser {
        return ResponseUser(
            id = 8,
            name = "name",
            trainerName = "trainer_name",
            email = "name@example.com"
        )
    }
}