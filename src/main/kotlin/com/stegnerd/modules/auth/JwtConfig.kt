package com.stegnerd.modules.auth

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.stegnerd.config.Config
import com.stegnerd.models.CredentialsResponse
import com.stegnerd.models.User
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.*

interface TokenProvider {
    fun createTokens(user: User): CredentialsResponse
    fun verifyToken(token: String): Int?
}

object JwtConfig: TokenProvider, KoinComponent {

    private val config by inject<Config>()

    private val secret = config.jwtSecret
    private val issuer = config.jwtIssuer
    private val audience = config.jwtAudience
    private const val validityInMs: Long = 3600000L * 6L // 6h
    private const val refreshValidityInMs: Long = 3600000L * 24L * 14L // 14 days
    private val algorithm = Algorithm.HMAC512(secret)

    val verifier: JWTVerifier = JWT
        .require(algorithm)
        .withAudience(audience)
        .withIssuer(issuer)
        .build()

    override fun verifyToken(token: String): Int? {
        return verifier.verify(token).claims["id"]?.asInt()
    }

    /**
     * Produce token and refresh token for this combination of User and Account
     */
    override fun createTokens(user: User) = CredentialsResponse(
        createToken(user, getTokenExpiration()),
        createToken(user, getTokenExpiration(refreshValidityInMs))
    )

    private fun createToken(user: User, expiration: Date) = JWT.create()
        .withSubject("Authentication")
        .withAudience(audience)
        .withIssuer(issuer)
        .withClaim("id", user.id)
        .withClaim("name", user.name)
        .withExpiresAt(expiration)
        .sign(algorithm)

    /**
     * Calculate the expiration Date based on current time + the given validity
     */
    private fun getTokenExpiration(validity: Long = validityInMs) = Date(System.currentTimeMillis() + validity)
}