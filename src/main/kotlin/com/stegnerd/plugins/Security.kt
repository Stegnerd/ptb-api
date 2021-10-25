package com.stegnerd.plugins

import com.auth0.jwt.interfaces.JWTVerifier
import com.stegnerd.api.user.UserApi
import com.stegnerd.config.Config
import com.stegnerd.database.DatabaseProviderContract
import com.stegnerd.modules.auth.authenticationModule
import io.ktor.application.Application
import io.ktor.application.install
import io.ktor.auth.Authentication
import io.ktor.features.CORS
import org.koin.ktor.ext.inject

fun Application.configureSecurity() {
    val userApi by inject<UserApi>()
    val databaseProvider by inject<DatabaseProviderContract>()
    val jwtVerifier by inject<JWTVerifier>()
    val config by inject<Config>()

    install(Authentication){
        authenticationModule(config.jwtRealm,userApi, databaseProvider, jwtVerifier)
    }

    install(CORS){
        anyHost()
    }
}