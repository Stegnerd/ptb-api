package com.stegnerd.api.user

import com.stegnerd.models.RegisterUserRequest
import com.stegnerd.models.UpdateUserRequest
import com.stegnerd.models.User

interface UserApi {
    fun getUserByID(ID: Int): User?
    fun getUserByEmail(email: String): User?
    fun createAccount(newUser: RegisterUserRequest): User?
    fun updateAccount(userID: Int, updateUserRequest: UpdateUserRequest): User?
    fun deleteAccount(userID: Int): Boolean
}