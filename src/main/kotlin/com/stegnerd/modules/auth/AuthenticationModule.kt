package com.stegnerd.modules.auth

import com.auth0.jwt.interfaces.JWTVerifier
import com.stegnerd.api.user.UserApi
import com.stegnerd.database.DatabaseProviderContract
import io.ktor.server.auth.AuthenticationConfig
import io.ktor.server.auth.jwt.jwt

fun AuthenticationConfig.authenticationModule(
    realmConfig: String,
    userApi: UserApi,
    databaseProvider: DatabaseProviderContract,
    tokenVerifier: JWTVerifier
) {
    jwt("jwt") {
        verifier(tokenVerifier)
        realm = realmConfig
        validate {credential ->
            credential.payload.getClaim("id").asInt()?.let { userId ->
                // do database query to find Principal subclass
                databaseProvider.dbQuery {
                    userApi.getUserByID(userId)
                }
            }
        }
    }
}