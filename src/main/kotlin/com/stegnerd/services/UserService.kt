package com.stegnerd.services

import com.stegnerd.DatabaseFactory.dbQuery
import com.stegnerd.models.RegisterRequest
import com.stegnerd.models.User
import com.stegnerd.models.Users
import com.stegnerd.models.Users.email
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.mindrot.jbcrypt.BCrypt

class UserService {
    suspend fun getAllUsers(): List<User> = dbQuery {
        Users.selectAll().map { toUser(it) }
    }

    suspend fun getUserByEmail(email: String): User? = dbQuery {
        Users.select {
            (Users.email eq email)
        }.mapNotNull { toUser(it) }
            .singleOrNull()
    }

    suspend fun createUser(newUser: RegisterRequest): User = dbQuery {
        Users.insert {
            it[name] = newUser.trainerName
            it[email] = newUser.email
            it[password] = BCrypt.hashpw(newUser.password, BCrypt.gensalt())
            it[active] = true
        }

        Users.select {
            (email eq email)
        }.mapNotNull { toUser(it) }
            .single()
    }

    private fun toUser(row: ResultRow): User =
        User(
            id = row[Users.id],
            name = row[Users.name],
            email = row[Users.email],
            active = row[Users.active],
            password = row[Users.password]
        )
}