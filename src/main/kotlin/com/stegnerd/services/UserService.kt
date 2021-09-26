package com.stegnerd.services

import com.stegnerd.DatabaseFactory.dbQuery
import com.stegnerd.db.models.User
import com.stegnerd.db.models.Users
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll

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

    private fun toUser(row: ResultRow): User =
        User(
            id = row[Users.id],
            name = row[Users.name],
            email = row[Users.email],
            active = row[Users.active],
            password = row[Users.password]
        )
}