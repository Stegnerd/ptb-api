package com.stegnerd.plugins

import com.stegnerd.models.LoginRequest
import com.stegnerd.services.UserService
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.routing
import org.mindrot.jbcrypt.BCrypt

fun Application.configureRouting() {
    val jwtIssuer = environment.config.property("jwt.issuer").getString()
    val jwtAudience = environment.config.property("jwt.audience").getString()
    val simpleJWT = SimpleJWT(environment.config.property("jwt.secret").getString())
    val userService = UserService();


    routing {
        get("/") {
            call.respondText("Hello World!")
        }
        post("/login") {
            val post = call.receive<LoginRequest>()

            val user = userService.getUserByEmail(post.email)
            if (user == null || !BCrypt.checkpw(post.password, user.password)) {
                error("Invalid Credentials")
            }

            call.respond(mapOf("token" to simpleJWT.sign(user.id, jwtAudience, jwtIssuer)))
        }
    }
}
