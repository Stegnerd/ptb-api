package com.stegnerd.modules.user

import com.stegnerd.api.user.UserApi
import com.stegnerd.models.ResponseUser
import com.stegnerd.models.UpdateUserRequest
import com.stegnerd.models.toResponseUser
import com.stegnerd.modules.BaseController
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

interface UserController {
    suspend fun updateAccount(userID: Int, payload: UpdateUserRequest): ResponseUser
    suspend fun deleteAccount(userID: Int)
}

class UserControllerImpl : BaseController(), UserController, KoinComponent {

    private val userApi by inject<UserApi>()

    override suspend fun updateAccount(userID: Int, payload: UpdateUserRequest): ResponseUser {
        val user = dbQuery {
            userApi.updateAccount(userID, payload) ?: throw UnknownError("Internal server error.")
        }
        return user.toResponseUser()
    }

    override suspend fun deleteAccount(userID: Int) {
        dbQuery {
            try {
                userApi.deleteAccount(userID)
            }catch (e: Exception) {
                throw UnknownError("Internal server error.")
            }
        }
    }
}