package com.stegnerd

import com.stegnerd.database.DatabaseProviderContract
import com.stegnerd.models.User
import com.stegnerd.plugins.configureMonitoring
import com.stegnerd.plugins.configureRouting
import com.stegnerd.plugins.configureSecurity
import com.stegnerd.plugins.configureSerialization
import io.ktor.application.Application
import io.ktor.application.ApplicationCall
import io.ktor.auth.authentication
import org.koin.ktor.ext.inject

fun Application.configurePlugins() {
    val databaseProvider by inject<DatabaseProviderContract>()
    // connect to db and migrate here
    databaseProvider.init()

    // ktor features/plugins
    // Logging
    configureMonitoring()
    // Serialization
    configureSerialization()
    // Auth
    configureSecurity()
    // Routes
    configureRouting()
}

// this allows us to do -> call.user.x (puts user in call context)
val ApplicationCall.user get() = authentication.principal<User>()!!