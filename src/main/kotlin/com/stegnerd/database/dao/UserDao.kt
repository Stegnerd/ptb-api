package com.stegnerd.database.dao

import com.stegnerd.models.RegisterUserRequest
import com.stegnerd.models.UpdateUserRequest
import com.stegnerd.models.User
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.javatime.datetime
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.update
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime

interface UserDao {
    fun getUserByID(userID: Int): User?
    fun getUserByEmail(userEmail: String): User?
    fun insertUser(newUser: RegisterUserRequest): Int?
    fun updateUser(userID: Int, updateUserRequest: UpdateUserRequest): User?
    fun deleteUser(userID: Int): Boolean
}

/**
 * Creates an identity table named trainer where pk is id as an uuid
 */
object Users : Table(), UserDao {
    val id: Column<Int> = integer("id").autoIncrement()
    val name: Column<String> = varchar("name", 64)
    val trainerName: Column<String> = varchar("trainer_name", 32)
    val email: Column<String> = varchar("email", 64)
    val password: Column<String> = varchar("password", 128)
    val active: Column<Boolean> = bool("active").default(true)
    val created: Column<LocalDateTime> =
        datetime("created_utc").clientDefault { ZonedDateTime.now(ZoneId.of("UTC")).toLocalDateTime() }
    val updated: Column<LocalDateTime> =
        datetime("updated_utc").clientDefault { ZonedDateTime.now(ZoneId.of("UTC")).toLocalDateTime() }
    override val primaryKey = PrimaryKey(id)

    override fun getUserByID(userID: Int): User? {
        return select {
            (id eq userID)
        }.mapNotNull {
            it.mapRowToUser()
        }.singleOrNull()
    }

    override fun getUserByEmail(userEmail: String): User? {
        return select {
            (email eq userEmail)
        }.mapNotNull {
            it.mapRowToUser()
        }.singleOrNull()
    }

    override fun insertUser(newUser: RegisterUserRequest): Int? {
        return (insert {
            it[name] = newUser.name
            it[trainerName] = newUser.trainerName
            it[email] = newUser.email
            it[password] = newUser.password
        })[id]
    }

    override fun updateUser(userID: Int, updateUserRequest: UpdateUserRequest): User? {
        update({ id eq userID }) { user ->
            updateUserRequest.name?.let { user[name] = it }
            updateUserRequest.trainerName?.let { user[trainerName] = it }
        }
        return getUserByID(userID)
    }

    override fun deleteUser(userID: Int): Boolean {
        return deleteWhere { (id eq userID) } > 0
    }
}

private fun ResultRow.mapRowToUser() =
    User(
        id = this[Users.id],
        name = this[Users.name],
        trainerName = this[Users.trainerName],
        email = this[Users.email],
        active = this[Users.active],
        password = this[Users.password],
        created = this[Users.created],
        updated = this[Users.updated]
    )



