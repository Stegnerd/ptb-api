package com.stegnerd.modules.user

import com.stegnerd.api.user.UserApi
import com.stegnerd.models.UpdateUserRequest
import com.stegnerd.models.User
import com.stegnerd.modules.BaseController
import com.stegnerd.utils.InvalidUserException
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

interface UserController {
    suspend fun updateAccount(userID: Int, payload: UpdateUserRequest): User
    suspend fun deleteAccount(userID: Int)
}

class UserControllerImpl : BaseController(), UserController, KoinComponent {

    private val userApi by inject<UserApi>()

    override suspend fun updateAccount(userID: Int, payload: UpdateUserRequest): User = dbQuery{
        userApi.updateAccount(userID, payload)?.let {
            it
        } ?: throw InvalidUserException("Failed to update account.")
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