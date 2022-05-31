package com.stegnerd.modules.registration

import com.stegnerd.models.LoginUserRequest
import com.stegnerd.models.RefreshTokenRequest
import com.stegnerd.models.RegisterUserRequest
import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Routing
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import org.koin.ktor.ext.inject

fun Routing.registrationModule() {

    val controller by inject<RegistrationController>()

    route("/auth") {
        post("register") {
            val registerRequest = call.receive<RegisterUserRequest>()
            val userCreated = controller.register(registerRequest)
            call.respond(userCreated)
        }

        post("login") {
            val loginRequest = call.receive<LoginUserRequest>()
            val loginTokenResponse = controller.login(loginRequest)
            call.respond(loginTokenResponse)
        }

        post("token") {
            val refreshTokenRequest = call.receive<RefreshTokenRequest>()
            val credentialsResponse = controller.refreshToken(refreshTokenRequest)
            call.respond(credentialsResponse)
        }
    }
}