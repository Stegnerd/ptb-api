package com.stegnerd.models

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table

/**
 * Creates an identity table named trainer where pk is id as an uuid
 */
object Users : Table() {
    val id: Column<Int> = integer("id").autoIncrement()
    val name: Column<String> = varchar("name", 64)
    val email: Column<String> = varchar("email", 64)
    val password: Column<String> = varchar("password", 128)
    val active: Column<Boolean> = bool("active")
    override val primaryKey = PrimaryKey(id)
}

@Serializable
data class User(
    val id: Int,
    val name: String,
    val email: String,
    val password: String,
    val active: Boolean
)