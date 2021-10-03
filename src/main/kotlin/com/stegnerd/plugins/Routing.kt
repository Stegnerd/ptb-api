package com.stegnerd.plugins

import com.stegnerd.models.LoginRequest
import com.stegnerd.models.RegisterRequest
import com.stegnerd.services.UserService
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.auth.authenticate
import io.ktor.auth.jwt.JWTPrincipal
import io.ktor.auth.principal
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.response.respondText
import io.ktor.routing.Route
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.routing
import org.mindrot.jbcrypt.BCrypt

fun Application.configureRouting() {
    val jwtIssuer = environment.config.property("jwt.issuer").getString()
    val jwtAudience = environment.config.property("jwt.audience").getString()
    val jwtSecret = environment.config.property("jwt.secret").getString()

    routing {
        get("/") {
            call.respondText("Hello World!")
        }
        authRoutes(jwtIssuer, jwtAudience, jwtSecret)
        trainerRoutes()
    }
}

fun Route.authRoutes(issuer: String, audience: String, secret: String) {
    val simpleJWT = SimpleJWT(secret)
    val userService = UserService()

    post("/login") {
        val post = call.receive<LoginRequest>()

        val user = userService.getUserByEmail(post.email)
        if (user == null || !BCrypt.checkpw(post.password, user.password)) {
            error("Invalid Credentials")
        }

        call.respond(mapOf("token" to simpleJWT.sign(user.id, user.email, audience, issuer)))
    }
    post("/register") {
        val post = call.receive<RegisterRequest>()

        val user = userService.createUser(post)
        call.respond(mapOf("token" to simpleJWT.sign(user.id, user.email, audience, issuer)))
    }
}

fun Route.trainerRoutes() {
    val userService = UserService()

    authenticate {
        get("/trainer") {
            val principal = call.principal<JWTPrincipal>() ?: error("No principal decoded")
            val email = principal.payload.claims["email"]?.asString() ?: error("No email set")

            val user = userService.getUserByEmail(email) ?: error("No user found with that email")

            call.respond(mapOf("user" to user))
        }
    }
}