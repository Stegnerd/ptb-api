package com.stegnerd.database

import com.stegnerd.database.dao.Users
import com.stegnerd.models.User
import com.stegnerd.stub.model.AuthStub.generateRegisterUserRequest
import com.stegnerd.stub.model.UserStub.generateUpdateUserRequest
import org.assertj.core.api.Assertions.assertThat
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.mindrot.jbcrypt.BCrypt
import java.time.LocalDateTime

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserDaoTest : BaseDaoTest() {

    @Test
    fun `getUserByID returns user when user exists`() {
        transaction {
            createSchema()
            val existingUser = insertTestUser()
            val user = Users.getUserByID(existingUser.id)
            user?.let {
                assertThat(user).isNotNull
            } ?: throw IllegalStateException("ID cannot be null.")
        }
    }

    @Test
    fun `getUserByID returns null when user not found`() {
        transaction {
            createSchema()
            val id = 0
            val user = Users.getUserByID(id)
            assertThat(user).isNull()
        }
    }

    @Test
    fun `getUserByEmail returns user when user exists`() {
        transaction {
            createSchema()
            val existingUser = insertTestUser()
            val user = Users.getUserByEmail(existingUser.email)
            user?.let {
                assertThat(user).isNotNull
            } ?: throw IllegalStateException("ID cannot be null.")
        }
    }

    @Test
    fun `getUserByEmail returns null when user not found`() {
        transaction {
            createSchema()
            val email = ""
            val user = Users.getUserByEmail(email)
            assertThat(user).isNull()
        }
    }

    @Test
    fun `insertUser when data is valid `() {
        transaction {
            createSchema()
            val newUser = generateRegisterUserRequest()
            val userID = Users.insertUser(newUser)
            userID?.let {
                val user = Users.getUserByID(userID)
                assertThat(user?.id).isEqualTo(userID)
                assertThat(user?.name).isEqualTo(newUser.name)
                assertThat(user?.email).isEqualTo(newUser.email)
            } ?: throw IllegalStateException("ID cannot be null.")
        }
    }

    @Test
    fun `updateUser returns updated user when successful`() {
        transaction {
            createSchema()
            val existingUser = insertTestUser()
            val updateUserRequest = generateUpdateUserRequest()
            val updatedUser = Users.updateUser(existingUser.id, updateUserRequest)

            assertThat(existingUser.name).isNotEqualTo(updatedUser?.name)
        }
    }

    @Test
    fun `deleteUser returns true when user deleted`() {
        transaction {
            createSchema()
            val existingUser = insertTestUser()
            val delete = Users.deleteUser(existingUser.id)
            assertThat(delete).isTrue
        }
    }

    @Test
    fun `deletUser returns false when no users deleted`() {
        transaction {
            createSchema()
            val id = 8
            val delete = Users.deleteUser(id)
            assertThat(delete).isFalse
        }
    }

    override fun createSchema() {
        SchemaUtils.create(Users)
    }

    override fun dropSchema() {
        SchemaUtils.drop(Users)
    }

    private fun insertTestUser(): User {
        val newUser = generateRegisterUserRequest()
        var id = 0
        transaction {
            val userID = Users.insertUser(newUser)
            userID?.let {
                id = userID
            }
        }
        return User(
            id,
            name = newUser.name,
            email = newUser.email,
            password =  BCrypt.hashpw(newUser.password, BCrypt.gensalt()),
            active = true,
            created = LocalDateTime.now(),
            updated = LocalDateTime.now()
        )
    }
}