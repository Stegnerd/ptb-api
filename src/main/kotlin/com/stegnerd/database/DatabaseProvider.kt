package com.stegnerd.database

import com.stegnerd.config.Config
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.flywaydb.core.Flyway
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.transaction
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

interface DatabaseProviderContract {
    fun init()
    suspend fun <T> dbQuery(block: () -> T): T
}

class DatabaseProvider: DatabaseProviderContract, KoinComponent {

    private val config by inject<Config>()

    override fun init() {
        Database.connect(hikari())
        // auto migration when spinning up db
        val flyway = Flyway.configure().dataSource(config.jdbcUrl, config.dbUser, config.dbPassword).load()
        flyway.migrate()
    }

    private fun hikari(): HikariDataSource {
        val hikariConfig = HikariConfig()
        hikariConfig.driverClassName = "org.postgresql.Driver"
        hikariConfig.jdbcUrl = config.jdbcUrl
        hikariConfig.username = config.dbUser
        hikariConfig.password = config.dbPassword
        hikariConfig.maximumPoolSize = 3
        // auto commit sql operations
        hikariConfig.isAutoCommit = false
        // no dirty reads allowed. Only see data that was committed prior to start of statement
        hikariConfig.transactionIsolation = "TRANSACTION_READ_COMMITTED"
        hikariConfig.validate()
        return HikariDataSource(hikariConfig)
    }

    /**
     * this function will suspend the current coroutine and launch a new one on the special IO thread pool
     * - which will then block whilst the database transaction is performed.
     * When the result is ready, the coroutine is resumed and returned to the initial caller.
     */
    override suspend fun <T> dbQuery(block: () -> T): T =
        withContext(Dispatchers.IO) {
            transaction { block() }
        }
}