package com.stegnerd.plugins

import com.auth0.jwt.interfaces.JWTVerifier
import com.stegnerd.api.user.UserApi
import com.stegnerd.config.Config
import com.stegnerd.database.DatabaseProviderContract
import com.stegnerd.modules.auth.authenticationModule
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.auth.Authentication
import io.ktor.server.plugins.cors.routing.CORS
import io.ktor.server.plugins.defaultheaders.DefaultHeaders
import io.ktor.server.plugins.forwardedheaders.ForwardedHeaders
import org.koin.ktor.ext.inject

fun Application.configureSecurity() {
    val userApi by inject<UserApi>()
    val databaseProvider by inject<DatabaseProviderContract>()
    val jwtVerifier by inject<JWTVerifier>()
    val config by inject<Config>()

    install(Authentication){
        authenticationModule(config.jwtRealm,userApi, databaseProvider, jwtVerifier)
    }

    install(ForwardedHeaders)
    install(DefaultHeaders)
    install(CORS){
        allowMethod(HttpMethod.Options)
        allowMethod(HttpMethod.Get)
        allowMethod(HttpMethod.Post)
        allowMethod(HttpMethod.Put)
        allowMethod(HttpMethod.Delete)
        allowHeader(HttpHeaders.AccessControlAllowHeaders)
        allowHeader(HttpHeaders.AccessControlAllowOrigin)
        allowHeader(HttpHeaders.Authorization)
        allowHeader(HttpHeaders.ContentType)
        allowCredentials = true
        allowSameOrigin = true
        allowNonSimpleContentTypes = true
        anyHost() // @TODO: Don't do this in production if possible. Try to limit it.
    }
}