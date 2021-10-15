package com.stegnerd.database

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach

abstract class BaseDaoTest {

    @BeforeEach
    open fun setup() {
        // in memory database used for testing.
        Database.connect("jdbc:h2:mem:test;MODE=PostgreSQL;DATABASE_TO_LOWER=true", driver = "org.h2.Driver", user = "root", password = "")
    }

    @AfterEach
    open fun tearDown() {
        transaction {
            dropSchema()
        }
    }

    abstract fun createSchema()
    abstract fun dropSchema()
}