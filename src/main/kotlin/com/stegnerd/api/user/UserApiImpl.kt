package com.stegnerd.api.user

import com.stegnerd.database.dao.UserDao
import com.stegnerd.models.RegisterUserRequest
import com.stegnerd.models.UpdateUserRequest
import com.stegnerd.models.User
import com.stegnerd.statuspages.InvalidUserException
import com.stegnerd.utils.PasswordWrapperContract
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

object UserApiImpl: UserApi, KoinComponent {
    private val usersDao by inject<UserDao>()
    private val passwordWrapper by inject<PasswordWrapperContract>()

    override fun getUserByID(ID: Int): User? {
        return usersDao.getUserByID(ID)
    }

    override fun getUserByEmail(email: String): User? {
        return usersDao.getUserByEmail(email)
    }

    override fun createAccount(newUser: RegisterUserRequest): Boolean {
        val encryptedUser = newUser.copy(password = passwordWrapper.encryptPassword(newUser.password))
        usersDao.insertUser(encryptedUser) ?: throw InvalidUserException("Error while creating user.")
        return true
    }

    override fun updateAccount(userID: Int, updateUserRequest: UpdateUserRequest): User? {
        return usersDao.updateUser(userID, updateUserRequest)
    }

    override fun deleteAccount(userID: Int): Boolean {
        return usersDao.deleteUser(userID)
    }
}