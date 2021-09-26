package com.stegnerd

import com.typesafe.config.ConfigFactory
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.config.HoconApplicationConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.flywaydb.core.Flyway
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseFactory {

    private val appConfig = HoconApplicationConfig(ConfigFactory.load())
    private val dbUrl = appConfig.property("db.jdbcUrl").getString()
    private val dbUser = appConfig.property("db.dbUser").getString()
    private val dbPassword = appConfig.property("db.dbPassword").toString()

    fun init() {
        Database.connect(hikari())
        // auto migration when spinning up db
        val flyway = Flyway.configure().dataSource(dbUrl, dbUser, dbPassword).load()
        flyway.migrate()
    }

    private fun hikari(): HikariDataSource {
        val config = HikariConfig()
        config.driverClassName = "org.postgresql.Driver"
        config.jdbcUrl = dbUrl
        config.username = dbUser
        config.password = dbPassword
        config.maximumPoolSize = 3
        // auto commit sql operations
        config.isAutoCommit = false
        // no dirty reads allowed. Only see data that was committed prior to start of statement
        config.transactionIsolation = "TRANSACTION_READ_COMMITTED"
        config.validate()
        return HikariDataSource(config)
    }

    /**
     * this function will suspend the current coroutine and launch a new one on the special IO thread pool
     * - which will then block whilst the database transaction is performed.
     * When the result is ready, the coroutine is resumed and returned to the initial caller.
     */
    suspend fun <T> dbQuery(block: () -> T): T =
        withContext(Dispatchers.IO) {
            transaction { block() }
        }
}