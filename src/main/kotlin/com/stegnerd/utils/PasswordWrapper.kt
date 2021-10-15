package com.stegnerd.utils

import org.mindrot.jbcrypt.BCrypt

object PasswordWrapper : PasswordWrapperContract{
    override fun validatePassword(input: String, hashedPassword: String): Boolean {
        return BCrypt.checkpw(input, hashedPassword)
    }

    override fun encryptPassword(password: String): String {
        return BCrypt.hashpw(password, BCrypt.gensalt())
    }
}

interface PasswordWrapperContract {
    fun validatePassword(input: String, hashedPassword: String): Boolean
    fun encryptPassword(password: String): String
}